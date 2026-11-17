package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Scanner

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var selectedCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weather = WeatherUi()


        selectedCity = resources.getStringArray(R.array.cities).first()


        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCity = parent?.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    suspend fun loadWeather(): WeatherUi? {
        val apiKey = getString(R.string.api_key)
        val city = getString(R.string.default_city)
        val encodedCity = URLEncoder.encode(selectedCity, "UTF-8")
        val weatherURL =
            "https://api.openweathermap.org/data/2.5/weather" +
                    "?q=$encodedCity&appid=$apiKey&units=metric&lang=ru"

        var connection: HttpURLConnection? = null
        return try {
            withContext(Dispatchers.IO) {
                connection = URL(weatherURL).openConnection() as HttpURLConnection
                connection!!.connectTimeout = 8000
                connection!!.readTimeout = 8000

                val code = connection!!.responseCode
                if (code != HttpURLConnection.HTTP_OK) {
                    Log.e("mytag", "HTTP $code")
                    return@withContext null
                }


                val data = Scanner(connection!!.inputStream).use { it.nextLine() }

                if (data.isBlank()) return@withContext null
                Log.d("mytag", data)

                parseWeather(data)
            }
        } catch (e: Exception) {
            Log.e("mytag", "Network error", e)
            null
        } finally {
            connection?.disconnect()
        }
    }

    private fun parseWeather(json: String): WeatherUi? {
        return try {
            val root = JSONObject(json)
            val main = root.getJSONObject("main")
            val weatherObj = root.getJSONArray("weather").getJSONObject(0)
            val wind = root.optJSONObject("wind")

            WeatherUi(
                city = root.optString("name", ""),
                tempText = getString(R.string.temp_format, main.getDouble("temp")),
                description = weatherObj.optString("description", ""),
                feelsLikeText = getString(
                    R.string.feels_like_format,
                    main.optDouble("feels_like", main.getDouble("temp"))
                ),
                humidityText = getString(
                    R.string.humidity_format,
                    main.optInt("humidity", 0)
                ),
                windText = getString(
                    R.string.wind_format,
                    wind?.optDouble("speed", 0.0) ?: 0.0
                ),
                iconCode = weatherObj.optString("icon", ""),
                windDeg = (wind?.optDouble("deg", 0.0) ?: 0.0).toFloat()
            )
        } catch (e: Exception) {
            Log.e("mytag", "Parse error", e)
            null
        }
    }

    fun onClick(v: View) {
        GlobalScope.launch(Dispatchers.IO) {
            val weather = loadWeather()
            withContext(Dispatchers.Main) {
                if (weather == null) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.error_network,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@withContext
                }
                binding.weather = weather
                binding.ivClouds.setImageResource(cloudIconRes(weather.iconCode))

            }
        }
    }


    private fun cloudIconRes(iconCode: String): Int = when {
        iconCode.startsWith("01") -> R.drawable.ic_clouds_clear
        iconCode.startsWith("02") || iconCode.startsWith("03") -> R.drawable.ic_clouds_few
        iconCode.startsWith("04") -> R.drawable.ic_clouds_cloudy
        iconCode.startsWith("09") || iconCode.startsWith("10") -> R.drawable.ic_clouds_rain
        iconCode.startsWith("11") -> R.drawable.ic_clouds_storm
        iconCode.startsWith("13") -> R.drawable.ic_clouds_snow
        iconCode.startsWith("50") -> R.drawable.ic_clouds_mist
        else -> R.drawable.ic_clouds_cloudy
    }
}


