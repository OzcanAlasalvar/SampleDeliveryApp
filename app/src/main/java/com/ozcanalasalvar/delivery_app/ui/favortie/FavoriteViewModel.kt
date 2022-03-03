package com.ozcanalasalvar.delivery_app.ui.favortie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class FavoriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    var stations = repository.getFavoriteStations().asLiveData()

    fun onFavoriteStatusChanged(station: Station) = viewModelScope.launch {
        val currentStation = station.copy()
        currentStation.isFavorite = !station.isFavorite
        repository.updateStation(currentStation)
    }

}