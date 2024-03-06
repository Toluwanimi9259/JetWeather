package com.techafresh.jetweather.util

class WeatherWrapper<T, Boolean, E : Exception>(
    var data : T? = null,
    var loading : Boolean? = null,
    var e : E? = null
)