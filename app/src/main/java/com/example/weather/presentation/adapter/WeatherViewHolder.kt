package com.example.weather.presentation.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.WeatherState

class WeatherViewHolder(view: View):RecyclerView.ViewHolder(view) {
    val hour = view.findViewById<TextView>(R.id.hour)
    val temperature = view.findViewById<TextView>(R.id.temp)
    val state = view.findViewById<TextView>(R.id.state)
    val possibility = view.findViewById<TextView>(R.id.rain_possibility)
    val icon = view.findViewById<ImageView>(R.id.weather_icon)

    fun render(weatherModel: WeatherState){
        hour.text = weatherModel.hour
        temperature.text = weatherModel.temperature.toString() + "°"
        state.text = weatherModel.state.weatherDesc
        possibility.text = weatherModel.possibility.toString() + "%"

        icon.setImageResource(weatherModel.state.iconRes)

    }
}