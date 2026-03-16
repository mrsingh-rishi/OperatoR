package com.rishi.operater.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Local in-memory repository placeholder for active session metadata.
 */
class SessionRepository {
    private val _currentTask = MutableStateFlow<String?>(null)
    val currentTask: StateFlow<String?> = _currentTask.asStateFlow()

    fun setCurrentTask(task: String?) {
        _currentTask.value = task
    }
}
