package com.rishi.operater.service.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Isolated OCR module using ML Kit Text Recognition.
 *
 * The processor accepts captured bitmaps and returns normalized text output that
 * can be consumed by UI, planner, or any future decision pipeline.
 */
class MlKitOcrProcessor {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun recognize(bitmap: Bitmap): OcrTextResult {
        val image = InputImage.fromBitmap(bitmap, 0)
        val visionText = suspendCancellableCoroutine { continuation ->
            recognizer
                .process(image)
                .addOnSuccessListener { result ->
                    if (continuation.isActive) {
                        continuation.resume(result)
                    }
                }
                .addOnFailureListener { error ->
                    if (continuation.isActive) {
                        continuation.resumeWithException(error)
                    }
                }
        }

        val normalizedLines = visionText.textBlocks
            .flatMap { block -> block.lines }
            .map { line -> line.text.trim() }
            .filter { text -> text.isNotBlank() }

        return OcrTextResult(
            fullText = visionText.text.trim(),
            lines = normalizedLines,
        )
    }
}
