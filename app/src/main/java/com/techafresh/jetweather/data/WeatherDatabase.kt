package com.techafresh.jetweather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techafresh.jetweather.model.Favourite
import com.techafresh.jetweather.model.Unit

@Database(entities = [Favourite::class, Unit::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun dao() : WeatherDao
}