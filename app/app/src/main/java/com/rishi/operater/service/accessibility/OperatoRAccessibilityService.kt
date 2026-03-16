package com.rishi.operater.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Accessibility bridge stub for future UI tree observation and controlled interaction.
 */
class OperatoRAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Intentionally empty. Future implementation will process allowed accessibility events.
    }

    override fun onInterrupt() {
        // Intentionally empty. Future implementation will release accessibility resources.
    }
}
