package com.techafresh.jetweather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(tableName = "favourites")
data class Favourite(

    @Nonnull
    @PrimaryKey
    @ColumnInfo(name = "city")
    val city : String,

    @ColumnInfo(name = "country")
    val country : String
)