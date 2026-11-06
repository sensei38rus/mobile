package com.example.counter

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {


    private lateinit var text_counter: TextView

    private lateinit var increment_counter: Button

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        text_counter = findViewById(R.id.text_counter)
        increment_counter = findViewById(R.id.increment_counter)

        val provider = ViewModelProvider(this)
        viewModel = provider.get(MainViewModel::class.java)


        observeViewModel()

        initView()



    }

    fun observeViewModel() {
        viewModel.counter.observe(this, Observer {
            text_counter.text = it.toString()
        })
    }

    fun initView() {
        increment_counter.setOnClickListener {
            viewModel.onIncrementClicked()
        }
    }

}