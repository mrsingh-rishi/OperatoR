package com.rishi.operater.data.repository

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.data.model.AgentLifecycleStatus
import com.rishi.operater.data.model.PermissionStatus
import com.rishi.operater.data.model.ScreenCaptureCapabilityStatus
import com.rishi.operater.service.accessibility.OperatoRAccessibilityService
import com.rishi.operater.service.projection.ScreenCaptureController
import android.content.pm.PackageManager

class SystemCapabilityRepository(
    private val appContext: Context,
    private val screenCaptureController: ScreenCaptureController
) {
    fun getAccessibilityStatus(): PermissionStatus {
        val enabledServices = Settings.Secure.getString(
            appContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return PermissionStatus.DENIED

        val expectedService = ComponentName(appContext, OperatoRAccessibilityService::class.java)
            .flattenToString()
            .lowercase()

        val isEnabled = enabledServices
            .split(':')
            .map { it.lowercase() }
            .any { it == expectedService }

        return if (isEnabled) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
    }

    fun getMicrophonePermissionStatus(): PermissionStatus {
        val result = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.RECORD_AUDIO
        )
        return if (result == PackageManager.PERMISSION_GRANTED) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
    }

    fun getScreenCaptureCapabilityStatus(): ScreenCaptureCapabilityStatus {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return ScreenCaptureCapabilityStatus.UNAVAILABLE
        }

        return if (screenCaptureController.isCapturing) {
            ScreenCaptureCapabilityStatus.ACTIVE
        } else {
            ScreenCaptureCapabilityStatus.READY
        }
    }

    fun getAgentLifecycleStatus(sessionState: AgentSessionState): AgentLifecycleStatus = when (sessionState) {
        AgentSessionState.Idle -> AgentLifecycleStatus.IDLE
        AgentSessionState.Stopped,
        AgentSessionState.Error -> AgentLifecycleStatus.STOPPED
        AgentSessionState.Preparing,
        AgentSessionState.Running,
        AgentSessionState.AwaitingConfirmation -> AgentLifecycleStatus.RUNNING
    }
}
