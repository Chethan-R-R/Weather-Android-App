package com.example.weather_android_app

data class DailyWeatherData(
    val list:List<SingleWeather>
)
data class SingleWeather(
    val main:Main,
    val weather: List<Weather>,
    val wind: Wind,
    val dt_txt:String
)

