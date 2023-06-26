package com.example.weather_android_app


data class WeatherData(
    val weather:List<Weather>,
    val main:Main,
    val wind:Wind,
    val dt:Long,
    val timezone: Int,
    val name: String
)

data class Weather(
    val main:String
)

data class Main(
    val temp:Float,
    val feels_like:Float,
    val humidity:Int
)

data class Wind(
    val speed: Float
)