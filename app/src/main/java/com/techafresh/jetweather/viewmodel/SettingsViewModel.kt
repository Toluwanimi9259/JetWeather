package com.techafresh.jetweather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techafresh.jetweather.repository.FavRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import com.techafresh.jetweather.model.Unit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: FavRepo
): ViewModel() {
    private val _unitList = MutableStateFlow<List<Unit>>(emptyList())
    val unitList = _unitList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getUnits().distinctUntilChanged()
                .collect { listOfUnits ->
                    if (listOfUnits.isNullOrEmpty()) {
                        Log.d("TAG", ":Empty list ")
                    }else {
                        _unitList.value = listOfUnits

                    }

                }

        }
    }

    fun insertUnit(unit: Unit) = viewModelScope.launch { repo.insertUnit(unit) }
    fun updateUnit(unit: Unit) = viewModelScope.launch { repo.updateUnit(unit) }
    fun deleteUnit(unit: Unit) = viewModelScope.launch { repo.deleteUnit(unit) }
    fun deleteAllUnits() = viewModelScope.launch { repo.deleteAllUnits() }


}