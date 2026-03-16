package com.rishi.operater.service.projection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ScreenCapturePermissionState(
    val isSupported: Boolean,
    val isPermissionGranted: Boolean,
)

/**
 * Coordinates MediaProjection permission state.
 *
 * This controller intentionally does not start frame streaming yet.
 * It only owns permission intent creation and consent result handling so
 * virtual display / ImageReader plumbing can be added in a follow-up.
 */
class ScreenCaptureController(context: Context) {
    private val appContext = context.applicationContext
    private val mediaProjectionManager: MediaProjectionManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appContext.getSystemService(MediaProjectionManager::class.java)
        } else {
            null
        }

    private var mediaProjection: MediaProjection? = null

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
        _permissionState.value = _permissionState.value.copy(isPermissionGranted = true)
    }

    fun revokePermission() {
        releaseProjection()
    }

    private fun releaseProjection() {
        val activeProjection = mediaProjection ?: run {
            _permissionState.value = _permissionState.value.copy(isPermissionGranted = false)
            return
        }

        mediaProjection = null
        activeProjection.unregisterCallback(projectionCallback)
        activeProjection.stop()

        _permissionState.value = _permissionState.value.copy(isPermissionGranted = false)
    }
}
