package com.example.weather_android_app

data class Places(

    val results: List<Result>,
    val total_results: Int
)

data class Result(
    val formatted:String,
    val geometry: Geometry
)

data class Geometry(
    val lat: String,
    val lng: String
)