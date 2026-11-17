package com.example.newweather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.newweather.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apikey = "28bcfb3e2cdfd3eeac38e750d5ce9c39"

    private var currentTemp = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val cities = resources.getStringArray(R.array.cities)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.citySpinner.adapter = adapter


        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = cities[position]
                getWeather(selected)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.checkBox.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked){
                binding.windDeg.visibility = View.VISIBLE
            }else{
                binding.windDeg.visibility = View.INVISIBLE
            }

        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_cel -> {
                   val celsius = toCelsius(currentTemp)
                   binding.temperature.text = "${celsius}°C"
                   currentTemp =  celsius
                }

                R.id.radio_fah -> {
                    val fahrenheit = toFahrenheit(currentTemp)
                    binding.temperature.text = "${fahrenheit}°F"
                    currentTemp = fahrenheit
                }
            }
        }



    }

    fun getWeather(city: String){
        lifecycleScope.launch {
            try {
                val data = RetrofitInstance.retrofitService.getWeather(city,apikey)
                currentTemp = data.main.temp
                binding.temperature.text = "${currentTemp}°C"
                binding.windDeg.text = "${data.wind.deg}°"
            }catch (e: Exception){
                Log.d("mytag",e.message.toString())
            }
        }
    }

    fun toFahrenheit(celsius: Double): Double{
        return celsius * 9/5 + 32
    }

    fun toCelsius(fahrenheit: Double): Double{
        return (fahrenheit - 32) * 5/9
    }


}