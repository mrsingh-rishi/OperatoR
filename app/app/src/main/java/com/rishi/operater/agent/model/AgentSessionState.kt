package com.rishi.operater.agent.model

enum class AgentSessionState {
    Idle,
    Preparing,
    Running,
    AwaitingConfirmation,
    Stopped,
    Error
}
