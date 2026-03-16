package com.rishi.operater.agent.planner

/**
 * Placeholder for deterministic action planning output.
 * A future planner can map model reasoning + current UI state into executable steps.
 */
data class ActionPlan(
    val goal: String,
    val steps: List<String>
)
