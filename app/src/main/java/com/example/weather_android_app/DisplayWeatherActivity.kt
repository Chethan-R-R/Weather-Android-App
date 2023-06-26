package com.example.weather_android_app

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_android_app.databinding.ActivityWeatherDisplayBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class DisplayWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherDisplayBinding
    private lateinit var appInfo:ApplicationInfo

    private  var isDay:Boolean=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appInfo=applicationContext.packageManager.getApplicationInfo(applicationContext.packageName,
            PackageManager.GET_META_DATA)
        val dailyFragment=DailyFragment()
        val lat=intent.getStringExtra("lat")
        val lon=intent.getStringExtra("lon")

        var addRemove=false
        binding.showDailyBtn.setOnClickListener {
            if(addRemove){
                addRemove=false
                binding.datalayout.visibility=View.VISIBLE
                binding.datetime.visibility=View.VISIBLE
                binding.placeName.visibility=View.VISIBLE
                supportFragmentManager.beginTransaction()
                    .remove(dailyFragment)
                    .commit()
            }else{
                addRemove=true
                binding.datalayout.visibility=View.GONE
                binding.placeName.visibility=View.GONE
                binding.datetime.visibility=View.GONE
                val bundle=Bundle()
                bundle.putString("lat",lat)
                bundle.putString("lon",lon)
                bundle.putBoolean("isDay",isDay)
                dailyFragment.arguments=bundle
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer,dailyFragment)
                    .commit()
            }
        }
        fetchWeather(lat, lon)
    }

    private fun convertTime(shiftSeconds: Int?):String {
        val currentTimeMillis = System.currentTimeMillis()
        val shiftedTimeMillis = currentTimeMillis + ((shiftSeconds?.times(1000))?.toLong() ?:1 )

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return dateFormat.format(Date(shiftedTimeMillis))
    }
    private fun fetchWeather(lat: String?, lon: String?) {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiInterface::class.java)

        val caller =
            retrofitBuilder.getData(lat ?: "", lon ?: "", appInfo.metaData["WeatherAPI"].toString())

        caller.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val res = response.body()

                val actualTime=convertTime(res?.timezone)
                isDay=(actualTime.substring(11,13).toInt()>6 && actualTime.substring(11,13).toInt()<18)
                if(isDay){
                    binding.parentlayout.setBackgroundResource(R.drawable.sun_bg)
                    binding.datalayout.setBackgroundResource(R.drawable.round_black)
                    binding.temperature.setTextColor(Color.WHITE)
                    binding.weather.setTextColor(Color.WHITE)
                    binding.wind.setTextColor(Color.WHITE)
                    binding.humidity.setTextColor(Color.WHITE)
                    binding.feelsLike.setTextColor(Color.WHITE)
                    binding.datetime.setTextColor(Color.BLACK)
                    binding.placeName.setTextColor(Color.BLACK)
                }else{
                    binding.parentlayout.setBackgroundResource(R.drawable.moon_bg)
                    binding.datalayout.setBackgroundResource(R.drawable.round_white)
                    binding.temperature.setTextColor(Color.BLACK)
                    binding.weather.setTextColor(Color.BLACK)
                    binding.wind.setTextColor(Color.BLACK)
                    binding.humidity.setTextColor(Color.BLACK)
                    binding.feelsLike.setTextColor(Color.BLACK)
                    binding.datetime.setTextColor(Color.WHITE)
                    binding.placeName.setTextColor(Color.WHITE)
                }
                binding.temperature.text = res?.main?.temp?.minus(272.15)?.toInt().toString() + "°C"
                binding.feelsLike.text =
                    "Feels Like " + res?.main?.feels_like?.minus(272.15)?.toInt().toString() + "°C"
                binding.humidity.text = "Humidity " + res?.main?.humidity.toString() + "%"
                binding.wind.text = "Wind " + res?.wind?.speed.toString() + "km/h"
                binding.weather.text = "Weather " + res?.weather?.get(0)?.main
                binding.datetime.text=actualTime
                binding.placeName.text=res?.name
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                binding.temperature.text="Internet Required"
                Log.i("error", t.toString())
            }
        })
    }
}