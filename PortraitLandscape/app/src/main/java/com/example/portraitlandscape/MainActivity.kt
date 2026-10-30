package com.example.portraitlandscape

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var picture: ImageView
    private lateinit var spinner: Spinner

    // Список drawable-ресурсов — порядок должен совпадать с массивом pictures
    private val pictures = intArrayOf(R.drawable.car1, R.drawable.car2, R.drawable.car3)
    // Индекс текущей картинки (-1 = исходный котик)
    private var currentIndex = -1

    private val prefs by lazy {
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        picture = findViewById(R.id.picture)
        spinner = findViewById(R.id.pictures_list)

        adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        // Восстанавливаем сохранённое состояние
        currentIndex = prefs.getInt(KEY_INDEX, -1)
        applyPicture(currentIndex, notifySpinner = true)
    }

    /** Меняет картинку по индексу; -1 — исходный котик */
    private fun applyPicture(index: Int, notifySpinner: Boolean = false) {
        currentIndex = index
        if (index in pictures.indices) {
            picture.setImageResource(pictures[index])
        } else {
            picture.setImageResource(R.drawable.squarecat)
        }

        // Синхронизируем спиннер, не вызывая рекурсивно onItemSelected
        if (notifySpinner) {
            val target = if (index in pictures.indices) index else -1
            spinner.setSelection(target.coerceAtLeast(0))
        }

        // Сохраняем выбор
        prefs.edit().putInt(KEY_INDEX, currentIndex).apply()
    }

    fun onChangePictureClick(v: View) {
        // Следующая картинка по кругу
        val next = if (currentIndex >= pictures.size - 1) 0 else currentIndex + 1
        applyPicture(next, notifySpinner = true)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this, "выбран элемент $position", Toast.LENGTH_SHORT).show()
        if (position != currentIndex) {
            applyPicture(position, notifySpinner = false)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "не выбран элемент", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val KEY_INDEX = "selected_picture_index"
    }
}