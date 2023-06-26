package com.example.weather_android_app

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class DailyListAdapter(private var data:List<SingleWeather>,private val isDay:Boolean):RecyclerView.Adapter<DailyListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val dayView=itemView.findViewById<ConstraintLayout>(R.id.day_item)
        private val temperature=dayView.findViewById<TextView>(R.id.dayTemperature)
        private val date=dayView.findViewById<TextView>(R.id.dayDate)
        private val dayWeather=dayView.findViewById<TextView>(R.id.dayWeather)
        private val wind=dayView.findViewById<TextView>(R.id.dayWind )
        private val humidity=dayView.findViewById<TextView>(R.id.dayHumidity)

        fun bind(weather:SingleWeather){

            if(isDay){
                dayView.setBackgroundResource(R.drawable.curved_black)
                date.setTextColor(Color.WHITE)
                temperature.setTextColor(Color.WHITE)
                humidity.setTextColor(Color.WHITE)
                wind.setTextColor(Color.WHITE)
                dayWeather.setTextColor(Color.WHITE)
            }else{
                dayView.setBackgroundResource(R.drawable.curved_white)
            }
            date.text=weather.dt_txt.substring(0,10)
            dayWeather.text="Weather "+weather.weather[0].main
            humidity.text="Humidity "+weather.main.humidity.toString()+"%"
            temperature.text=weather.main.temp.minus(273.15).toInt().toString()+"°C"
            wind.text="Wind "+String.format("%.2f",weather.wind.speed)+" km/h"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.day_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}