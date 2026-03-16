package com.rishi.operater.agent.session

import com.rishi.operater.agent.model.AgentSessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Coordinates local session lifecycle.
 *
 * Future integration points:
 * - AccessibilityService: semantic UI tree ingestion.
 * - MediaProjection controller: frame ingestion.
 * - Gemini Live transport: multimodal reasoning stream.
 * - Deterministic executor: safe action execution.
 */
class AgentSessionManager {
    private val _state = MutableStateFlow(AgentSessionState.Idle)
    val state: StateFlow<AgentSessionState> = _state.asStateFlow()

    private var currentTask: String? = null

    fun start(task: String) {
        currentTask = task
        _state.value = AgentSessionState.Preparing
    }

    fun markRunning() {
        _state.value = AgentSessionState.Running
    }

    fun stop() {
        currentTask = null
        _state.value = AgentSessionState.Stopped
    }

    fun getCurrentTask(): String? = currentTask
}
