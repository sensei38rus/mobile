package com.example.guessthenumber

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    var begin: Int = 0
    var end: Int = 0
    var isFinished: Boolean = false
    lateinit var tvQuestion: TextView
    lateinit var buttonYes: Button
    lateinit var buttonNo: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        begin = intent.getIntExtra("begin",1)
        end = intent.getIntExtra("end", 100)

        tvQuestion = findViewById(R.id.question)

        buttonYes = findViewById(R.id.yes)
        buttonNo = findViewById(R.id.no)

        askQuestion()

    }



    fun finishWithAnswer(number: Int) {
        isFinished = true
        tvQuestion.text = "Ваше число: $number"
        buttonYes.visibility = View.GONE
        buttonNo.visibility = View.GONE
    }


    fun askQuestion(){

        if (isFinished) return


        if (end - begin <= 1) {

            if (begin == end) {
                finishWithAnswer(begin)
            } else {
                tvQuestion.text = "Ваше число — $begin?"
            }
            return
        }

        val mid = (end+begin)/2
        tvQuestion.text = "Ваше число больше ${mid}?"

    }
    fun onYesNoClick(view: View) {
        if (isFinished) return


        if (end - begin <= 1) {
            when (view.id) {
                R.id.yes -> finishWithAnswer(begin)
                R.id.no  -> finishWithAnswer(end)
            }
            return
        }


        val mid = (begin + end) / 2

        when (view.id) {
            R.id.yes -> {

                begin = mid+1
                Log.d("range","${begin},${end}")
            }
            R.id.no -> {
                end = mid
                Log.d("range","${begin},${end}")
            }

        }

        askQuestion()

    }
}