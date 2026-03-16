package com.rishi.operater.service.projection

/**
 * Placeholder MediaProjection coordinator.
 *
 * Intended lifecycle:
 * 1) Request user consent through MediaProjectionManager.
 * 2) Create virtual display + image reader.
 * 3) Forward frames through callback for multimodal reasoning.
 * 4) Stop and release projection resources safely.
 */
class ScreenCaptureController {
    var isCapturing: Boolean = false
        private set

    fun startCapture() {
        // MediaProjection setup will be implemented in a future phase.
        isCapturing = true
    }

    fun stopCapture() {
        isCapturing = false
    }
}
