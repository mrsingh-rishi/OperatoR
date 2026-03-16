package com.rishi.operater.data.repository

import android.graphics.Bitmap
import com.rishi.operater.service.ocr.MlKitOcrProcessor
import com.rishi.operater.service.ocr.OcrProcessingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OcrRepository(
    private val processor: MlKitOcrProcessor,
) {
    private val _processingState = MutableStateFlow(OcrProcessingState())
    val processingState: StateFlow<OcrProcessingState> = _processingState.asStateFlow()

    suspend fun processBitmap(bitmap: Bitmap) {
        _processingState.value = _processingState.value.copy(
            isProcessing = true,
            lastFailureReason = null,
        )

        runCatching {
            processor.recognize(bitmap)
        }.onSuccess { result ->
            _processingState.value = _processingState.value.copy(
                isProcessing = false,
                lastResult = result,
                lastFailureReason = null,
            )
        }.onFailure { error ->
            _processingState.value = _processingState.value.copy(
                isProcessing = false,
                lastFailureReason = error.message ?: "OCR failed",
            )
        }
    }
}
