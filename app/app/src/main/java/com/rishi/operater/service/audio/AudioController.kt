package com.rishi.operater.service.audio

/**
 * Placeholder for microphone capture and audio playback orchestration.
 *
 * Future Gemini Live integration can stream microphone audio and play model responses
 * through a controlled session-aware pipeline.
 */
class AudioController {
    var isRecording: Boolean = false
        private set

    fun startRecording() {
        // AudioRecord initialization will be added in later phases.
        isRecording = true
    }

    fun stopRecording() {
        isRecording = false
    }
}
