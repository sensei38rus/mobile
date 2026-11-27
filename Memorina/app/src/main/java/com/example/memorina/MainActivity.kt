package com.example.memorina

import android.graphics.Color
import android.icu.text.ListFormatter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val faces = listOf(
        R.drawable.fivespades, R.drawable.joker, R.drawable.ninehearts, R.drawable.fourclubs,
        R.drawable.eightdiamonds, R.drawable.jackspades, R.drawable.heartsking, R.drawable.ace
    )

    private var firstCard: ImageView? = null
    private var secondCard: ImageView? = null
    private var isChecking = false
    private var matchedCount = 0

    lateinit var winText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val deck = (faces + faces).shuffled()

        //setContentView(R.layout.activity_main)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        winText = TextView(this)
        winText.apply {
            text = "Победа"
            textSize = 30f
            visibility = View.INVISIBLE
            gravity = Gravity.CENTER
        }

        val params = LinearLayout.LayoutParams(0, 250).apply {
            weight = 1f
            marginStart = 20
            marginEnd = 20
            topMargin = 50
        }

        val cardViews = ArrayList<ImageView>()
        for (faceRes in deck) {
            val card = ImageView(this).apply {
                layoutParams = params
                setImageResource(R.drawable.back)
                tag = faceRes
                setOnClickListener(cardListener)
            }
            cardViews.add(card)
        }

        val rows = Array(4, { LinearLayout(applicationContext)})

        var count = 0
        for (view in cardViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count ++
        }
        for (row in rows) {
            layout.addView(row)
        }


        setContentView(layout)


    }


    private val cardListener = View.OnClickListener {
        val card = it as ImageView

        if (isChecking) return@OnClickListener
        if (card == firstCard || card == secondCard) return@OnClickListener
        if (!card.isClickable) return@OnClickListener

        card.setImageResource(card.tag as Int)

        when {
            firstCard == null -> firstCard = card
            secondCard == null -> {
                secondCard = card
                checkMatch()
            }
        }
    }


    private fun checkMatch() {
        val first = firstCard ?: return
        val second = secondCard ?: return
        isChecking = true

        lifecycleScope.launch {
            delay(800)

            if (first.tag == second.tag) {
                first.visibility = View.INVISIBLE
                second.visibility = View.INVISIBLE
                first.isClickable = false
                second.isClickable = false
                matchedCount++

            } else {
                first.setImageResource(R.drawable.back)
                second.setImageResource(R.drawable.back)
            }

            firstCard = null
            secondCard = null
            isChecking = false

            if (matchedCount == 8){
                winText.visibility = View.VISIBLE
                setContentView(winText)

            }
        }
    }
}
