package com.example.newweather

data class WeatherData(
    val name: String,
    val main: Main,
    val wind: Wind

)

data class Main(
    val temp: Double
)

data class Wind(
    val deg: Double
)
