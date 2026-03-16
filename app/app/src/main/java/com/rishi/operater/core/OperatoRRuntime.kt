package com.rishi.operater.core

import com.rishi.operater.agent.session.AgentSessionManager
import com.rishi.operater.data.repository.SessionRepository
import com.rishi.operater.service.audio.AudioController
import com.rishi.operater.service.projection.ScreenCaptureController

/**
 * Lightweight app-level runtime container.
 *
 * Keeps local architecture cohesive and ready for future Gemini Live wiring
 * without introducing DI framework complexity in this foundation phase.
 */
object OperatoRRuntime {
    val sessionManager: AgentSessionManager = AgentSessionManager()
    val sessionRepository: SessionRepository = SessionRepository()
    val screenCaptureController: ScreenCaptureController = ScreenCaptureController()
    val audioController: AudioController = AudioController()
}
