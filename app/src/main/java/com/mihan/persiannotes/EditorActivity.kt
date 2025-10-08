package com.mihan.persiannotes

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mihan.persiannotes.databinding.ActivityEditorBinding
import com.mihan.persiannotes.drawing.DrawingView
import com.mihan.persiannotes.ml.RecognitionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding
    private lateinit var recognitionManager: RecognitionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recognitionManager = RecognitionManager(this)

        binding.btnClear.setOnClickListener {
            (binding.drawingView as DrawingView).clear()
            binding.txtRecognized.text = ""
        }

        binding.btnRecognize.setOnClickListener {
            val strokes = (binding.drawingView as DrawingView).getStrokesForInk()
            CoroutineScope(Dispatchers.Main).launch {
                val text = recognitionManager.recognize(strokes)
                binding.txtRecognized.text = text
            }
        }

        binding.btnSave.setOnClickListener {
            val bmp = (binding.drawingView as DrawingView).exportBitmap()
            val f = File(filesDir, "note_${System.currentTimeMillis()}.png")
            FileOutputStream(f).use { out -> bmp.compress(Bitmap.CompressFormat.PNG, 90, out) }
            Toast.makeText(this, "Saved: ${f.absolutePath}", Toast.LENGTH_LONG).show()
        }

        binding.btnExportPdf.setOnClickListener {
            val bmp = (binding.drawingView as DrawingView).exportBitmap()
            val pdf = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bmp.width, bmp.height, 1).create()
            val page = pdf.startPage(pageInfo)
            page.canvas.drawBitmap(bmp, 0f, 0f, null)
            pdf.finishPage(page)
            val f = File(getExternalFilesDir(null), "export_${System.currentTimeMillis()}.pdf")
            FileOutputStream(f).use { pdf.writeTo(it) }
            pdf.close()
            Toast.makeText(this, "PDF exported: ${f.absolutePath}", Toast.LENGTH_LONG).show()
        }
    }
}
