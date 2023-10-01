package com.example.weather.API

data class WeatherData(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val precipitation_probability: List<Int>,
    val weathercode: List<Int>
)
