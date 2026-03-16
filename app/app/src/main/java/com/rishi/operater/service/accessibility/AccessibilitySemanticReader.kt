package com.rishi.operater.service.accessibility

import com.rishi.operater.service.accessibility.model.ScreenSemanticSnapshot
import kotlinx.coroutines.flow.StateFlow

/**
 * Read-only contract for clients that need the latest accessibility semantic state.
 */
interface AccessibilitySemanticReader {
    val snapshot: StateFlow<ScreenSemanticSnapshot>
    fun currentSnapshot(): ScreenSemanticSnapshot
}

