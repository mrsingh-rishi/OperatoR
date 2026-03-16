package com.rishi.operater.data.model

data class SystemStatus(
    val accessibility: PermissionStatus,
    val screenCapture: PermissionStatus,
    val audio: PermissionStatus
)
