package com.example.newweather

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query




interface WeatherApi{
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apikey: String,
        @Query("units") units: String = "metric"): WeatherData
}

object RetrofitInstance{


    private val base_url = "https://api.openweathermap.org/"
    val retrofit = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService = retrofit.create(WeatherApi::class.java)
}
