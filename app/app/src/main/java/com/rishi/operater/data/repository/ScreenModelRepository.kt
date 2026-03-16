package com.rishi.operater.data.repository

import com.rishi.operater.service.accessibility.AccessibilitySemanticReader
import com.rishi.operater.service.accessibility.model.ScreenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Read-only repository exposing the latest extracted screen model.
 */
class ScreenModelRepository(
    private val accessibilityReader: AccessibilitySemanticReader,
) {
    val latestScreenModel: Flow<ScreenModel> = accessibilityReader.snapshot.map { it.screenModel }

    fun currentScreenModel(): ScreenModel = accessibilityReader.currentSnapshot().screenModel
}
