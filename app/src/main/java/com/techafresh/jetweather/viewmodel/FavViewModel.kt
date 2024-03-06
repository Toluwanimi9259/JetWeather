package com.techafresh.jetweather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techafresh.jetweather.model.Favourite
import com.techafresh.jetweather.repository.FavRepo
import com.techafresh.jetweather.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(private val repo: FavRepo) : ViewModel(){
    private val _favList = MutableStateFlow<List<Favourite>>(emptyList())
    val favList = _favList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFavs().distinctUntilChanged()
                .collect { listOfFavs ->
                    if (listOfFavs.isNullOrEmpty()) {
                        Log.d("TAG", ": Empty favs ")
                    }else {
                        _favList.value = listOfFavs
                        Log.d("FAVS", ":${favList.value}")
                    }
                }
        }
    }
    fun addToFav(fav: Favourite) = viewModelScope.launch {
        repo.addToFav(fav)
        Log.d("TAG", "addToFav: In ViewModel")
    }
//    fun updateFavourite(favorite: Favourite) = viewModelScope.launch { repository.updateFavourite(favorite) }
    fun deleteFav(fav: Favourite) = viewModelScope.launch { repo.deleteFav(fav) }
}