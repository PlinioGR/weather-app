package com.example.weather.API

data class WeatherResponse(
    var latitude: Double,
    var longitude: Double,
    var elevation: Double,
    var generationtime_ms: Double,
    var utc_offset_seconds: Int,
    var timezone: String,
    var timezone_abbreviation: String,
    var hourly: WeatherData,
    var hourly_units: Any,
    var current_weather: Any,
)
