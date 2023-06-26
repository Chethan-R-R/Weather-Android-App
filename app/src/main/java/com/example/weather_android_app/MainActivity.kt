package com.example.weather_android_app

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_android_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appInfo:ApplicationInfo
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://api.opencagedata.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoApiInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appInfo=applicationContext.packageManager.getApplicationInfo(applicationContext.packageName,PackageManager.GET_META_DATA)


        binding.myRecycler.layoutManager = LinearLayoutManager(this)
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun afterTextChanged(s: Editable?) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    fetch()
                } catch (err: Exception) {
                    Log.i("error", err.toString())
                }
            }
        })

    }

    fun fetch() {
        val caller = retrofitBuilder.getData(
            binding.editText.text.toString(),
            appInfo.metaData["GeoAPI"].toString()
        )

        caller.enqueue(object : Callback<Places> {
            override fun onResponse(call: Call<Places>, response: Response<Places>) {
                val res = response.body()
                val adapter = SearchListAdapter(res?.results ?: emptyList()) {

                    val displayWeatherActivityIntent =
                        Intent(applicationContext, DisplayWeatherActivity::class.java)
                    displayWeatherActivityIntent.putExtra("lat", it.geometry.lat)
                    displayWeatherActivityIntent.putExtra("lon", it.geometry.lng)
                    startActivity(displayWeatherActivityIntent)
                }
                binding.myRecycler.adapter = adapter
            }

            override fun onFailure(call: Call<Places>, t: Throwable) {
                Log.i("error", t.toString())
            }
        })
    }

}