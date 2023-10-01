package com.example.weather.data

import com.example.weather.data.WeatherType

data class WeatherState(
    val hour:String,
    val temperature:Number,
    val state: WeatherType,
    val possibility:Number
)
