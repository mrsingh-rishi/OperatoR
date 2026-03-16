package com.rishi.operater.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

/**
 * Accessibility bridge in observation-only mode.
 *
 * This service only reads semantic state from the active window. It intentionally does not
 * perform actions, gestures, or text entry.
 */
class OperatoRAccessibilityService : AccessibilityService() {
    private val screenModelExtractor = ScreenModelExtractor()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow
        val foregroundPackageName = event?.packageName?.toString() ?: rootNode?.packageName?.toString()

        val screenModel = screenModelExtractor.extract(
            rootNode = rootNode,
            fallbackPackageName = foregroundPackageName,
        )

        AccessibilityObservationStore.updateSnapshot(
            foregroundPackageName = foregroundPackageName,
            rootNodeAvailable = rootNode != null,
            lastEventType = event?.eventType,
            screenModel = screenModel,
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        AccessibilityObservationStore.onServiceConnectionChanged(connected = true)
    }

    override fun onInterrupt() {
        AccessibilityObservationStore.onServiceConnectionChanged(connected = false)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        AccessibilityObservationStore.onServiceConnectionChanged(connected = false)
        return super.onUnbind(intent)
    }
}
