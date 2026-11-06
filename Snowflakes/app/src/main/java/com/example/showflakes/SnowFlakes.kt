package com.example.showflakes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random


data class Snowflake(var x: Float, var y: Float, val velocity: Float, val radius: Float, val color: Int)

lateinit var snow: Array<Snowflake>
val paint = Paint()
var h = 1000
var w = 1000

open class Snowflakes(ctx: Context) : View(ctx) {
    lateinit var moveTask: MoveTask

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLUE)
        for (s in snow) {
            paint.color = s.color
            canvas.drawCircle(s.x, s.y, s.radius, paint)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        h = bottom - top
        w = right - left
        val r = Random(0)
        snow = Array(10) {
            Snowflake(
                x = r.nextFloat() * w,
                y = r.nextFloat() * h,
                velocity = 15 + 10 * r.nextFloat(),
                radius = 30 + 20 * r.nextFloat(),
                color = Color.rgb(
                    200 + (r.nextFloat() * 75).toInt(),   // R: 200..255
                    220 + (r.nextFloat() * 55).toInt(),   // G: 220..255
                    255                                    // B: 255
                )
            )
        }
    }

    fun updatePositions() {
        for (s in snow) {
            val slowDown = (h - s.y) / h  // замедление у низа
            s.y += s.velocity * slowDown
            s.x += kotlin.math.sin((s.y / 100f).toDouble()).toFloat() * 2f  // блуждание

            if (s.y > h) {
                s.y = 0f
                s.x = Random.nextFloat() * w
            }
            if (s.x < 0) s.x = w.toFloat()
            if (s.x > w) s.x = 0f
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (!::moveTask.isInitialized || moveTask.status != AsyncTask.Status.RUNNING) {
                moveTask = MoveTask(this)
                moveTask.execute(50)
            }
        }
        return true
    }

    class MoveTask(val s: Snowflakes) : AsyncTask<Int, Int, Int>() {
        override fun doInBackground(vararg params: Int?): Int {
            val delay = params[0] ?: 200
            while (!isCancelled) {
                Thread.sleep(delay.toLong())
                s.updatePositions()

                @Suppress("WrongThread")
                s.postInvalidate()
            }
            return 0
        }
    }
}

