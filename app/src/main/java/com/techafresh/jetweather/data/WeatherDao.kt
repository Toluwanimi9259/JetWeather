package com.techafresh.jetweather.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.techafresh.jetweather.model.Favourite
import com.techafresh.jetweather.model.Unit
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM favourites")
    fun getFavs() : Flow<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFav(fav: Favourite)

    @Query("DELETE from favourites")
    suspend fun deleteAllFavorites()

    @Delete
    suspend fun deleteFavorite(fav: Favourite)

    // Unit table
    @Query("SELECT * from settings")
    fun getUnits(): Flow<List<Unit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: Unit)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUnit(unit: Unit)

    @Query("DELETE from settings")
    suspend fun deleteAllUnits()

    @Delete
    suspend fun deleteUnit(unit: Unit)


}