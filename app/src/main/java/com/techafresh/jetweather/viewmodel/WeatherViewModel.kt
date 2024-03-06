package com.techafresh.jetweather.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.repository.WeatherRepo
import com.techafresh.jetweather.util.WeatherWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(private val repo: WeatherRepo) : ViewModel(){
    suspend fun getWeatherData(city : String, units : String = "imperial") : WeatherWrapper<Weather , Boolean, Exception> {
        return repo.getWeatherData(city,units)
    }
}