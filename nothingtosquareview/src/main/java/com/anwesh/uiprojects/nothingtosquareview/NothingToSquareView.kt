package com.anwesh.uiprojects.nothingtosquareview

/**
 * Created by anweshmishra on 31/01/19.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.app.Activity
import android.content.Context

val nodes : Int = 5
val lines : Int = 4
val steps : Int = 2
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#00C853")
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Canvas.drawRotateLine(x : Float, y: Float, size : Float, scale : Float, paint : Paint) {
    save()
    translate(x, y)
    rotate(-90f * (1 - scale.divideScale(1, 2)))
    drawLine(0f, 0f, size * scale.divideScale(0, 2), 0f, paint)
    restore()
}

fun Canvas.drawVerticalLines(x : Float, y: Float, paint : Paint) {
    for (j in (0..steps - 1)) {
        val sx : Float = x * (1f - 2 * j)
        drawLine(sx, 0f, sx, y, paint)
    }
}


fun Canvas.drawNTSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    val xGap = (2 * size) / lines
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    save()
    translate(w/2, gap * (i + 1))
    for (j in (0..(steps - 1))) {
        val sc : Float = sc2.divideScale(j, 2)
        val y : Float = size * (1 - 2 * j) * sc
        drawVerticalLines(-size, y, paint)
        save()
        translate(-size, y)
        for (k in (0..(lines - 1))) {
            val sck : Float = sc1.divideScale(k, lines)
            drawRotateLine(k * xGap, y, xGap, sck, paint)
        }
        restore()
    }
    restore()
}

class NothingToSquareView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateValue(dir, lines * 2, steps)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}