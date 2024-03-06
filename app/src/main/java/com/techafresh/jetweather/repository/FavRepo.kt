package com.techafresh.jetweather.repository

import android.util.Log
import com.techafresh.jetweather.data.WeatherDao
import com.techafresh.jetweather.model.Favourite
import kotlinx.coroutines.flow.Flow
import com.techafresh.jetweather.model.Unit
import javax.inject.Inject

class FavRepo @Inject constructor(private val dao: WeatherDao) {
    suspend fun addToFav(fav : Favourite){
        dao.addToFav(fav)
        Log.d("TAG", "addToFav: In FavRepo")
    }

    suspend fun getFavs() : Flow<List<Favourite>> = dao.getFavs()

    suspend fun deleteAllFavourites() = dao.deleteAllFavorites()

    suspend fun deleteFav(favourite: Favourite) = dao.deleteFavorite(favourite)

    fun getUnits(): Flow<List<Unit>> = dao.getUnits()
    suspend fun insertUnit(unit: Unit) = dao.insertUnit(unit)
    suspend fun updateUnit(unit: Unit) = dao.updateUnit(unit)
    suspend fun deleteAllUnits() = dao.deleteAllUnits()
    suspend fun deleteUnit(unit: Unit) = dao.deleteUnit(unit)

}