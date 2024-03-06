package com.techafresh.jetweather.api

import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") query : String,
        @Query("units") units : String = "imperial",
        @Query("appid") appid : String = API_KEY
    ) : Weather
}