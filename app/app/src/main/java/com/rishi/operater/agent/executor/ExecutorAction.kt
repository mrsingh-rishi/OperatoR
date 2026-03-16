package com.rishi.operater.agent.executor

/**
 * Deterministic actions the native executor will support in later phases.
 */
sealed interface ExecutorAction {
    data class Tap(val x: Int, val y: Int) : ExecutorAction
    data class InputText(val value: String) : ExecutorAction
    data object ScrollDown : ExecutorAction
    data object NavigateBack : ExecutorAction
}
