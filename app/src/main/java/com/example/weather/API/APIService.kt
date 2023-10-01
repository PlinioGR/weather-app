package com.example.weather.API

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getWeather(@Url url:String): Response<WeatherResponse>
}