package com.example.weather_android_app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_android_app.databinding.FragmentDailyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat


class DailyFragment : Fragment() {
    private lateinit var binding:FragmentDailyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentDailyBinding.inflate(inflater,container,false)
        binding.dailyRecyclerView.layoutManager=LinearLayoutManager(context)
        fetchDaily(arguments?.getString("lat","")!!,arguments?.getString("lon","")!!)

        return binding.root

    }
    private fun maxWeather(rain:Int,thunderstorm:Int,clouds:Int,clear:Int):String{
        return if(rain>thunderstorm && rain>clouds && rain>clear){
            "Rain"
        }else if(thunderstorm>clouds && thunderstorm>clear){
            "Thunder Storm"
        }else if(clouds>clear){
            "Clouds"
        }else{
            "Clear"
        }
    }

    private fun meanDailyWeather(weatherList:List<SingleWeather>):MutableList<SingleWeather>{
        val filteredWeather= mutableListOf<SingleWeather>()
        var curDate=weatherList[0].dt_txt.substring(0,10)
        var temperature= 0.0F
        var wind=0.0F
        var humidity=0
        var count=0
        var rain=0
        var thunderstorm=0
        var clear=0
        var clouds=0
        for((index,weather) in weatherList.withIndex()){
            if(curDate!=weather.dt_txt.substring(0,10) || index==weatherList.size-1){
                filteredWeather.add(
                    SingleWeather(
                        main = Main(
                            temp = temperature/count,
                            humidity = humidity/count,
                            feels_like = temperature/count),
                        wind = Wind(speed = wind/count),
                        weather = listOf(Weather(main = maxWeather(rain,thunderstorm,clouds,clear))),
                        dt_txt = curDate
                    )
                )
                curDate=weather.dt_txt.substring(0,10)
                count=1
                rain=0
                thunderstorm=0
                clear=0
                clouds=0
                temperature=weather.main.temp
                humidity=weather.main.humidity
                wind=weather.wind.speed
                when(weather.weather[0].main){
                    "Rain"->rain++
                    "Clouds"->clouds++
                    "Clear"->clear++
                    "Thunderstorm"->thunderstorm++
                }
            }else{
                count++
                temperature+=weather.main.temp
                humidity+=weather.main.humidity
                wind+=weather.wind.speed
                when(weather.weather[0].main){
                    "Rain"->rain++
                    "Clouds"->clouds++
                    "Clear"->clear++
                    "Thunderstorm"->thunderstorm++
                }

            }

        }
        return filteredWeather
    }
    private fun fetchDaily(lat:String,lon:String){
        binding.loading.visibility=View.VISIBLE
        val retrofitBuilder=Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiInterface::class.java)

        val caller=retrofitBuilder.getDaily(lat,lon,"690f0bbbf328f62ed6accf3047d615bd")

        caller.enqueue(object :Callback<DailyWeatherData>{
            override fun onResponse(
                call: Call<DailyWeatherData>,
                response: Response<DailyWeatherData>
            ) {
                val res=response.body()
                        val adapter=DailyListAdapter(meanDailyWeather(res?.list?: emptyList()),arguments?.getBoolean("isDay",true)!!)
                        binding.dailyRecyclerView.adapter=adapter

                binding.loading.visibility=View.GONE
            }

            override fun onFailure(call: Call<DailyWeatherData>, t: Throwable) {
                Log.i("error",t.toString())
            }
        })
    }
}