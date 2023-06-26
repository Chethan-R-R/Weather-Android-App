package com.example.weather_android_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {
    @GET("data/2.5/weather")
    fun getData(
    @Query("lat") latitude:String,
    @Query("lon") longitude:String,
    @Query("appid") apikey:String
    ):Call<WeatherData>

    @GET("data/2.5/forecast")
    fun getDaily(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("appid") apikey:String
    ):Call<DailyWeatherData>
}