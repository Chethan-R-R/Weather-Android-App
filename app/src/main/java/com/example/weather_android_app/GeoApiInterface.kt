package com.example.weather_android_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApiInterface {
    @GET("geocode/v1/json")
    fun getData(
        @Query("q") query: String,
        @Query("key") apikey:String
    ):Call<Places>
}