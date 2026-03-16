package com.rishi.operater.service.ocr

data class OcrTextResult(
    val fullText: String,
    val lines: List<String>,
)

data class OcrProcessingState(
    val isProcessing: Boolean = false,
    val lastResult: OcrTextResult? = null,
    val lastFailureReason: String? = null,
)
