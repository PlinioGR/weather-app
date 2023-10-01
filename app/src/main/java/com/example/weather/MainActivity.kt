package com.example.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.API.APIService
import com.example.weather.API.getRetrofit
import com.example.weather.data.Location
import com.example.weather.data.WeatherState
import com.example.weather.presentation.adapter.WeatherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.weather.data.mappers.convertToWeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.MainScope
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        MainScope().launch {
            val location = getLocation()
            if (location != null) {
                getCurrentWeather(location)
            }
        }
    }

    private suspend fun getLocation():Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this@MainActivity, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return null
        }
        return suspendCoroutine { continuation ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this, OnSuccessListener { coordinates ->
                if (coordinates != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)

                        val city = if(!addresses.isNullOrEmpty()) {
                            addresses[0].locality
                        } else{
                            "Unknown"
                        }

                        val location = Location(coordinates.latitude.toString(), coordinates.longitude.toString(), city)
                        continuation.resume(location)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else {
                    Toast.makeText(this@MainActivity, "Not available location", Toast.LENGTH_SHORT).show()
                    continuation.resume(null)
                }
            })
        }
    }

    private fun getCurrentWeather(location: Location){
        val currentContext = this

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit()
                .create(APIService::class.java)
                .getWeather("forecast?latitude="+location.latitude+"&longitude="+location.longitude+"&hourly=temperature_2m,precipitation_probability,weathercode&models=best_match")

            val weather = call.body()

            runOnUiThread{
                if(call.isSuccessful && weather != null){
                    val weatherPredictions = weather.hourly
                    val weatherArray = convertToWeatherData(weatherPredictions)

                    showCurrentWeather(weatherArray[0], location.city)
                    initRecyclerView(weatherArray)
                }else{
                    //show error
                    Toast.makeText(currentContext, "Error getting data", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initRecyclerView(predictions:List<WeatherState>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerWeather)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = WeatherAdapter(predictions)
    }

    private fun showCurrentWeather(weatherModel: WeatherState, city: String){
        val view = findViewById<CardView>(R.id.card_view)
        val temperature = view.findViewById<TextView>(R.id.current_temp)
        val state = view.findViewById<TextView>(R.id.current_state)
        val icon = view.findViewById<ImageView>(R.id.current_weather_icon)
        val location = view.findViewById<TextView>(R.id.location)

        temperature.text = weatherModel.temperature.toString() + "°"
        state.text = weatherModel.state.weatherDesc
        icon.setImageResource(weatherModel.state.iconRes)
        location.text = city
    }

}