package com.example.weather.data.mappers

import android.os.Build
import com.example.weather.API.WeatherData
import com.example.weather.data.WeatherState
import com.example.weather.data.WeatherType
import java.text.SimpleDateFormat
import java.util.Calendar


fun convertToWeatherData(weather: WeatherData): MutableList<WeatherState> {
    val timeArray = weather.time
    val temperatureArray = weather.temperature_2m
    val weatherCodeArray = weather.weathercode
    val possibilityArray = weather.precipitation_probability

    val weatherDataList = mutableListOf<WeatherState>()

    val currentTime = Calendar.getInstance()
    val currentHour = SimpleDateFormat("HH:mm").format(currentTime.time)

    for (i in 0 until timeArray.size) {
        val time = timeArray.get(i)
        val pattern = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val dateTime = pattern.parse(time)
        val calendar = Calendar.getInstance()
        calendar.time = dateTime
        val hour = SimpleDateFormat("HH:mm").format(calendar.time)

        if(hour >= currentHour){
            val temperature = temperatureArray.get(i)
            val weatherCode = weatherCodeArray.get(i)
            val possibility = possibilityArray.get(i)
            val weatherData = WeatherState(hour, temperature, WeatherType.fromWMO(weatherCode), possibility)
            weatherDataList.add(weatherData)
        }
    }

    return weatherDataList

}