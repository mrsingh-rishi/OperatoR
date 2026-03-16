package com.rishi.operater.core

import android.content.Context
import com.rishi.operater.agent.session.AgentSessionManager
import com.rishi.operater.data.repository.OcrRepository
import com.rishi.operater.data.repository.ScreenModelRepository
import com.rishi.operater.data.repository.SessionRepository
import com.rishi.operater.service.accessibility.AccessibilityObservationStore
import com.rishi.operater.service.accessibility.AccessibilitySemanticReader
import com.rishi.operater.service.audio.AudioController
import com.rishi.operater.service.ocr.MlKitOcrProcessor
import com.rishi.operater.service.projection.ScreenCaptureController

/**
 * Lightweight app-level runtime container.
 *
 * Keeps local architecture cohesive and ready for future Gemini Live wiring
 * without introducing DI framework complexity in this foundation phase.
 */
object OperatoRRuntime {
    private var initialized = false

    val sessionManager: AgentSessionManager = AgentSessionManager()
    val sessionRepository: SessionRepository = SessionRepository()
    lateinit var screenCaptureController: ScreenCaptureController
        private set
    val audioController: AudioController = AudioController()
    val accessibilityReader: AccessibilitySemanticReader = AccessibilityObservationStore
    val screenModelRepository: ScreenModelRepository = ScreenModelRepository(accessibilityReader)
    val ocrRepository: OcrRepository = OcrRepository(MlKitOcrProcessor())

    fun initialize(context: Context) {
        if (initialized) {
            return
        }

        screenCaptureController = ScreenCaptureController(context)
        initialized = true
    }
}
