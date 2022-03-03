package com.ozcanalasalvar.delivery_app.ui.station

import androidx.lifecycle.*
import com.ozcanalasalvar.delivery_app.R
import com.ozcanalasalvar.delivery_app.data.local.pref.FilterPreferences
import com.ozcanalasalvar.delivery_app.data.local.pref.PreferencesManager
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.model.StationAndVehicle
import com.ozcanalasalvar.delivery_app.data.repository.Repository
import com.ozcanalasalvar.delivery_app.data.utils.network.NetworkResult
import com.ozcanalasalvar.delivery_app.utils.AppConst
import com.ozcanalasalvar.delivery_app.utils.ext.distance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class StationsViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    val searchQuery = MutableStateFlow("")

    private val stationFlow = searchQuery.flatMapLatest {
        repository.getStations(it)
    }

    val stations = stationFlow.asLiveData()

    val vehicleLiveData = getSpaceVehicle().asLiveData()
    private var spaceVehicle: StationAndVehicle? = null

    val prefStatusLiveData = getPrefStatus().asLiveData()
    private lateinit var prefStatus: FilterPreferences

    private val stationEventChannel = Channel<StationEvent>()
    val stationEvent = stationEventChannel.receiveAsFlow()

    init {
        fetchStations(true)
    }

    private fun fetchStations(forceUpdate: Boolean) {
        viewModelScope.launch {
            repository.fetchAndCacheStations(forceUpdate).collect {
                when (it) {
                    is NetworkResult.Error -> {
                        stationEventChannel.send(StationEvent.ShowNetworkError(it.error.toErrorMessage()))
                    }
                    is NetworkResult.Success -> {
                        stationEventChannel.send(StationEvent.HideLoading)
                    }
                    NetworkResult.Loading -> {
                        stationEventChannel.send(StationEvent.ShowLoading)
                    }
                }
            }
        }


    }

    private fun getPrefStatus(): Flow<FilterPreferences> {
        return flow {
            preferencesManager.preferencesFlow.collect {
                if (it.UGS == 0 || it.EUS == 0) {
                    showDeliveryFinishedDialog()
                    return@collect
                }
                prefStatus = it
                emit(it)
            }
        }
    }

    private fun getSpaceVehicle(): Flow<StationAndVehicle> {
        return flow {
            repository.getVehicle().collect {
                if (it.vehicle.durabilityValue == 0) {
                    showDeliveryFinishedDialog()
                    return@collect
                }
                spaceVehicle = it
                emit(it)
            }
        }
    }


    fun onStartDeliveryToStation(station: Station) {

        val vehicle = spaceVehicle!!.vehicle
        val currentStation = spaceVehicle!!.station


        var vehicleUGS = prefStatus.UGS
        val vehicleEUS = prefStatus.EUS

        val spendEus = currentStation!!.distance(station)


        if (spendEus > vehicleEUS) {
            showFailureMessage(R.string.no_eus)
            return
        }


        if (vehicleUGS == 0) {
            showFailureMessage(R.string.no_ugs)
            return
        }

        if (station.need > vehicleUGS) {
            station.stock += vehicleUGS
            vehicleUGS = 0
        } else {
            station.stock += station.need
            vehicleUGS -= station.need
        }

        station.need = (station.capacity - station.stock)


        vehicle.currentStationName = station.name

        viewModelScope.launch {
            repository.addOrUpdateVehicle(vehicle)

            preferencesManager.updateUGS(vehicleUGS)

            preferencesManager.updateEUS(vehicleEUS - spendEus)

            repository.updateStation(station)
        }


    }

    private fun showFailureMessage(msg: Int) = viewModelScope.launch {
        stationEventChannel.send(StationEvent.ShowActionFailureMessage(msg))
    }

    private fun showDeliveryFinishedDialog() = viewModelScope.launch {
        stationEventChannel.send(StationEvent.ShowDeliveryFinishedDialog)
    }

    fun onFavoriteStatusChanged(station: Station) = viewModelScope.launch {
        val currentStation = station.copy()
        currentStation.isFavorite = !station.isFavorite
        try {
            repository.updateStation(currentStation)
            stationEventChannel.send(StationEvent.ShowActionSuccessMessage(R.string.add_favorite_complete))
        } catch (e: Exception) {
            stationEventChannel.send(StationEvent.ShowActionFailureMessage(R.string.general_error))
        }

    }

    fun onReloadClick() {
        fetchStations(true)
    }

    fun sendDamageToVehicle() {
        viewModelScope.launch {
            spaceVehicle?.let {
                it.vehicle.durabilityValue -= AppConst.DS_DAMAGE
                repository.addOrUpdateVehicle(it.vehicle)
            }

        }
    }

    fun resetDelivery() {
        fetchStations(true)
        spaceVehicle?.vehicle?.let {
            it.durabilityValue = AppConst.INITIAL_DAMAGE_CAPACITY
            it.currentStationName = AppConst.INITIAL_STATION
            viewModelScope.launch {
                val eus = it.velocity * AppConst.EUS_FACTOR
                val ugs = it.capacity * AppConst.UGS_FACTOR
                val ds = it.durability * AppConst.DS_FACTOR

                preferencesManager.setUpPreferences(UGS = ugs, EUS = eus, DS = ds)
                repository.addOrUpdateVehicle(it)
            }

        }
    }


    sealed class StationEvent {
        object ShowLoading : StationEvent()
        object HideLoading : StationEvent()
        data class ShowNetworkError(val msg: Int) : StationEvent()
        data class ShowActionSuccessMessage(val msg: Int) : StationEvent()
        data class ShowActionFailureMessage(val msg: Int) : StationEvent()
        object ShowDeliveryFinishedDialog : StationEvent()
    }

}