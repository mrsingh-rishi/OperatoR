package com.rishi.operater.service.projection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

data class ScreenCapturePermissionState(
    val isSupported: Boolean,
    val isPermissionGranted: Boolean,
)

data class FrameCaptureMetadata(
    val timestampNanos: Long,
    val width: Int,
    val height: Int,
    val rowStride: Int,
    val pixelStride: Int,
)

data class FrameCaptureState(
    val isPipelineReady: Boolean = false,
    val isCaptureInProgress: Boolean = false,
    val captureCount: Int = 0,
    val lastCaptureSucceeded: Boolean = false,
    val lastFailureReason: String? = null,
    val lastFrameMetadata: FrameCaptureMetadata? = null,
)

/**
 * Coordinates MediaProjection permission state and one-shot frame capture.
 *
 * This implementation intentionally captures metadata-only frames on demand.
 * No continuous streaming, OCR, or external model integration is performed.
 */
class ScreenCaptureController(context: Context) {
    private val appContext = context.applicationContext
    private val mediaProjectionManager: MediaProjectionManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appContext.getSystemService(MediaProjectionManager::class.java)
        } else {
            null
        }

    private val captureThread = HandlerThread("OperatoR-ScreenCapture").apply { start() }
    private val captureHandler = Handler(captureThread.looper)

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null
    private val captureMutex = Mutex()

    private val projectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            releaseProjection()
        }
    }

    private val _permissionState = MutableStateFlow(
        ScreenCapturePermissionState(
            isSupported = mediaProjectionManager != null,
            isPermissionGranted = false,
        ),
    )
    val permissionState: StateFlow<ScreenCapturePermissionState> = _permissionState.asStateFlow()

    private val _frameCaptureState = MutableStateFlow(FrameCaptureState())
    val frameCaptureState: StateFlow<FrameCaptureState> = _frameCaptureState.asStateFlow()

    fun createPermissionIntent(): Intent? = mediaProjectionManager?.createScreenCaptureIntent()

    fun onPermissionResult(resultCode: Int, data: Intent?) {
        if (mediaProjectionManager == null) {
            return
        }

        if (resultCode != Activity.RESULT_OK || data == null) {
            _permissionState.value = _permissionState.value.copy(isPermissionGranted = false)
            return
        }

        val newProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
        if (newProjection == null) {
            _permissionState.value = _permissionState.value.copy(isPermissionGranted = false)
            return
        }

        releaseProjection()

        mediaProjection = newProjection
        mediaProjection?.registerCallback(projectionCallback, Handler(Looper.getMainLooper()))

        val pipelineReady = setupCapturePipeline()
        _permissionState.value = _permissionState.value.copy(isPermissionGranted = pipelineReady)
    }

    suspend fun captureFrame(): Boolean = captureMutex.withLock {
        val reader = imageReader
        if (_permissionState.value.isPermissionGranted.not() || reader == null) {
            _frameCaptureState.value = _frameCaptureState.value.copy(
                lastCaptureSucceeded = false,
                lastFailureReason = "Capture pipeline unavailable",
            )
            return false
        }

        _frameCaptureState.value = _frameCaptureState.value.copy(
            isCaptureInProgress = true,
            lastFailureReason = null,
        )

        val metadata = withContext(Dispatchers.Default) {
            withTimeoutOrNull(CAPTURE_TIMEOUT_MS) {
                suspendCancellableCoroutine<FrameCaptureMetadata?> { continuation ->
                    val listener = ImageReader.OnImageAvailableListener { sourceReader ->
                        val image = sourceReader.acquireLatestImage() ?: return@OnImageAvailableListener

                        sourceReader.setOnImageAvailableListener(null, null)
                        val plane = image.planes.firstOrNull()
                        val frameMetadata = FrameCaptureMetadata(
                            timestampNanos = image.timestamp,
                            width = image.width,
                            height = image.height,
                            rowStride = plane?.rowStride ?: 0,
                            pixelStride = plane?.pixelStride ?: 0,
                        )
                        image.close()

                        if (continuation.isActive) {
                            continuation.resume(frameMetadata)
                        }
                    }

                    reader.setOnImageAvailableListener(listener, captureHandler)
                    continuation.invokeOnCancellation {
                        reader.setOnImageAvailableListener(null, null)
                    }
                }
            }
        }

        return if (metadata != null) {
            _frameCaptureState.value = _frameCaptureState.value.copy(
                isCaptureInProgress = false,
                captureCount = _frameCaptureState.value.captureCount + 1,
                lastCaptureSucceeded = true,
                lastFailureReason = null,
                lastFrameMetadata = metadata,
            )
            true
        } else {
            _frameCaptureState.value = _frameCaptureState.value.copy(
                isCaptureInProgress = false,
                lastCaptureSucceeded = false,
                lastFailureReason = "Timed out waiting for frame",
            )
            false
        }
    }

    fun revokePermission() {
        releaseProjection()
    }

    private fun setupCapturePipeline(): Boolean {
        val activeProjection = mediaProjection ?: return false
        releaseCapturePipeline()

        val metrics = appContext.resources.displayMetrics
        val width = metrics.widthPixels.coerceAtLeast(1)
        val height = metrics.heightPixels.coerceAtLeast(1)
        val densityDpi = metrics.densityDpi.coerceAtLeast(1)

        val newImageReader = ImageReader.newInstance(
            width,
            height,
            PixelFormat.RGBA_8888,
            MAX_IMAGES,
        )

        val newVirtualDisplay = activeProjection.createVirtualDisplay(
            VIRTUAL_DISPLAY_NAME,
            width,
            height,
            densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            newImageReader.surface,
            null,
            captureHandler,
        )

        if (newVirtualDisplay == null) {
            newImageReader.close()
            _frameCaptureState.value = _frameCaptureState.value.copy(
                isPipelineReady = false,
                lastCaptureSucceeded = false,
                lastFailureReason = "Failed to create virtual display",
            )
            return false
        }

        imageReader = newImageReader
        virtualDisplay = newVirtualDisplay
        _frameCaptureState.value = _frameCaptureState.value.copy(
            isPipelineReady = true,
            lastFailureReason = null,
        )
        return true
    }

    private fun releaseProjection() {
        val activeProjection = mediaProjection ?: run {
            _permissionState.value = _permissionState.value.copy(isPermissionGranted = false)
            releaseCapturePipeline()
            return
        }

        mediaProjection = null
        releaseCapturePipeline()
        activeProjection.unregisterCallback(projectionCallback)
        activeProjection.stop()

        _permissionState.value = _permissionState.value.copy(isPermissionGranted = false)
    }

    private fun releaseCapturePipeline() {
        imageReader?.setOnImageAvailableListener(null, null)
        imageReader?.close()
        imageReader = null

        virtualDisplay?.release()
        virtualDisplay = null

        _frameCaptureState.value = _frameCaptureState.value.copy(
            isPipelineReady = false,
            isCaptureInProgress = false,
        )
    }

    companion object {
        private const val VIRTUAL_DISPLAY_NAME = "OperatoRVirtualDisplay"
        private const val MAX_IMAGES = 2
        private const val CAPTURE_TIMEOUT_MS = 1_500L
    }
}
