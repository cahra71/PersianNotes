package com.mihan.persiannotes.ml

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionOptions
import com.google.mlkit.vision.digitalink.Ink
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RecognitionManager(private val context: Context) {
    private val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("fa")
    private val model = DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
    private val recognizer = DigitalInkRecognition.getClient(DigitalInkRecognitionOptions.builder(model).build())

    suspend fun recognize(strokes: List<Ink.Stroke>): String = suspendCancellableCoroutine { cont ->
        val inkBuilder = Ink.builder()
        for (s in strokes) inkBuilder.addStroke(s)
        val ink = inkBuilder.build()

        val task: Task<com.google.mlkit.vision.digitalink.DigitalInkRecognitionResult> = recognizer.recognize(ink)
        task.addOnSuccessListener { result ->
            if (!cont.isCompleted) {
                val text = if (result.candidates.isNotEmpty()) result.candidates[0].text else ""
                cont.resume(text)
            }
        }.addOnFailureListener { e ->
            if (!cont.isCompleted) cont.resumeWithException(e)
        }
        cont.invokeOnCancellation { task.cancel() }
    }
}
