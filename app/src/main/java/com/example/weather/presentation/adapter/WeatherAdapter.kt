package com.example.weather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.WeatherState

class WeatherAdapter(private val predictions:List<WeatherState>): RecyclerView.Adapter<WeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return WeatherViewHolder(layoutInflater.inflate(R.layout.weather_item, parent, false))
    }

    override fun getItemCount(): Int = predictions.size


    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = predictions[position]
        holder.render(item)
    }
}