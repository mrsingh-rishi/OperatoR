package com.rishi.operater.service.accessibility

import com.rishi.operater.service.accessibility.model.CurrentAppState
import com.rishi.operater.service.accessibility.model.NodeInfoSummary
import com.rishi.operater.service.accessibility.model.ScreenSemanticSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory bridge between [OperatoRAccessibilityService] and app UI/runtime layers.
 */
object AccessibilityObservationStore : AccessibilitySemanticReader {
    private val _snapshot = MutableStateFlow(ScreenSemanticSnapshot())
    override val snapshot: StateFlow<ScreenSemanticSnapshot> = _snapshot.asStateFlow()

    override fun currentSnapshot(): ScreenSemanticSnapshot = _snapshot.value

    fun onServiceConnectionChanged(connected: Boolean) {
        val current = _snapshot.value
        _snapshot.value = current.copy(
            appState = current.appState.copy(accessibilityServiceConnected = connected),
            capturedAtMillis = System.currentTimeMillis()
        )
    }

    fun updateSnapshot(
        foregroundPackageName: String?,
        rootNodeAvailable: Boolean,
        nodeSummary: NodeInfoSummary,
    ) {
        _snapshot.value = ScreenSemanticSnapshot(
            appState = CurrentAppState(
                accessibilityServiceConnected = true,
                foregroundPackageName = foregroundPackageName,
                rootNodeAvailable = rootNodeAvailable,
            ),
            nodeSummary = nodeSummary,
            capturedAtMillis = System.currentTimeMillis(),
        )
    }
}

