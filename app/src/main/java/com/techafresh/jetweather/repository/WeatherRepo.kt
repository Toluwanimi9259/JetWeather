package com.techafresh.jetweather.repository

import android.util.Log
import com.techafresh.jetweather.api.WeatherAPI
import com.techafresh.jetweather.data.WeatherDao
import com.techafresh.jetweather.model.Favourite
import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.util.WeatherWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val api : WeatherAPI) {
    suspend fun getWeatherData(city : String, units : String) : WeatherWrapper<Weather , Boolean, Exception> {
        val response = try {
            api.getForecast(city,units = units)
        }catch (ex: Exception){
            Log.d("MYTAG", "getWeatherData: ${ex.localizedMessage}")
            return WeatherWrapper(e = ex)
        }
        Log.d("MYTAG", "getWeatherData: $response")
        return WeatherWrapper(data = response)
    }


}