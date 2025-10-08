package com.mihan.persiannotes.drawing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.mlkit.vision.digitalink.Ink

data class SimpleStroke(val points: List<PointF>)
data class PointF(val x: Float, val y: Float, val t: Long)

class DrawingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val currentPath = Path()
    private val paths = mutableListOf<Path>()
    private val strokes = mutableListOf<SimpleStroke>()
    private var currentPoints = mutableListOf<PointF>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (p in paths) canvas.drawPath(p, paint)
        canvas.drawPath(currentPath, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentPath.moveTo(x, y)
                currentPoints = mutableListOf(PointF(x, y, t))
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
                currentPoints.add(PointF(x, y, t))
            }
            MotionEvent.ACTION_UP -> {
                paths.add(Path(currentPath))
                strokes.add(SimpleStroke(currentPoints.toList()))
                currentPath.reset()
            }
        }
        invalidate()
        return true
    }

    fun clear() {
        paths.clear(); currentPath.reset(); strokes.clear(); invalidate()
    }

    fun exportBitmap(): Bitmap {
        val bmp = Bitmap.createBitmap(width.coerceAtLeast(1), height.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        draw(c)
        return bmp
    }

    fun getStrokesForInk(): List<Ink.Stroke> {
        val inkStrokes = mutableListOf<Ink.Stroke>()
        for (s in strokes) {
            val builder = Ink.Stroke.builder()
            for (p in s.points) {
                builder.addPoint(Ink.Point.create(p.x, p.y, p.t))
            }
            inkStrokes.add(builder.build())
        }
        return inkStrokes
    }
}
