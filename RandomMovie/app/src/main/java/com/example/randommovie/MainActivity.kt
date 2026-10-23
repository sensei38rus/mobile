package com.example.randommovie

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {


    private lateinit var tvMovie: TextView

    private lateinit var nextButton: Button

    private lateinit var resetButton: Button

    private lateinit var remainingMovies: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvMovie = findViewById(R.id.textView)
        nextButton = findViewById(R.id.next)
        resetButton = findViewById(R.id.reset)


        resetMovies()

        nextButton.setOnClickListener {
            showRandomMovie()
        }

        resetButton.setOnClickListener {
            resetMovies()
            tvMovie.text = getString(R.string.empty)
        }


    }

    private fun resetMovies() {
        remainingMovies = resources.getStringArray(R.array.movies).toMutableList()
    }


    private fun showRandomMovie() {
        if (remainingMovies.isEmpty()) {
            tvMovie.text = getString(R.string.no_more)
            return
        }

        val index = (remainingMovies.indices).random()
        val movie = remainingMovies.removeAt(index)
        tvMovie.text = movie
    }

}