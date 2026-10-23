package com.example.guessthenumber

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun onGuessClick(view: View){

        val begin = findViewById<EditText>(R.id.begin)
        val end = findViewById<EditText>(R.id.end)

        if (begin.text.isBlank() || end.text.isBlank()){
            Toast.makeText(this,"Введите диапазон", Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("begin", begin.text.toString().toInt())
            intent.putExtra("end", end.text.toString().toInt())
            startActivity(intent)
        }


    }
}