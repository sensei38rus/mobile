package com.example.weatherapp

data class WeatherUi(
    val city: String = "",
    val tempText: String = "",
    val description: String = "",
    val feelsLikeText: String = "",
    val humidityText: String = "",
    val windText: String = "",
    val iconCode: String = "",
    val windDeg: Float = 0f
)