package com.rishi.operater.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Accessibility bridge for OperateR.
 *
 * This service is intentionally no-op for now. In later phases it will:
 * - observe window/content changes
 * - extract semantic UI signals for planning
 * - expose sanitized state to the local agent session manager
 */
class OperatoRAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Intentionally blank until semantic UI observation is implemented.
    }

    override fun onInterrupt() {
        // Intentionally blank; future phase may stop active observation/capture pipelines.
    }
}
