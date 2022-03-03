package com.ozcanalasalvar.delivery_app.ui.vehicle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozcanalasalvar.delivery_app.data.local.pref.PreferencesManager
import com.ozcanalasalvar.delivery_app.data.model.Vehicle
import com.ozcanalasalvar.delivery_app.data.repository.Repository
import com.ozcanalasalvar.delivery_app.utils.AppConst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class VehicleViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val totalPoint = MutableLiveData(0)

    var vehicleName = ""

    var vehicleDurability = 0
        set(value) {
            field = value
            calculateTotalPoint()
        }

    var vehicleCapacity = 0
        set(value) {
            field = value
            calculateTotalPoint()
        }

    var vehicleVelocity = 0
        set(value) {
            field = value
            calculateTotalPoint()
        }


    private val createVehicleChannel = Channel<VehicleEvent>()
    val createVehicleEvent = createVehicleChannel.receiveAsFlow()

    private fun calculateTotalPoint() {
        totalPoint.value = vehicleDurability + vehicleCapacity + vehicleVelocity
    }

    fun createVehicle() {
        if (vehicleName.isBlank()) {
            showInValidInputMessage("Vehicle name cannot be empty !")
            return
        }

        if (vehicleVelocity == 0) {
            showInValidInputMessage("Vehicle velocity cannot be 0.")
            return
        }

        if (vehicleDurability == 0) {
            showInValidInputMessage("Vehicle durability cannot be 0.")
            return
        }

        if (vehicleCapacity == 0) {
            showInValidInputMessage("Vehicle capacity cannot be 0.")
            return
        }

        if (totalPoint.value!! > 15) {
            showInValidInputMessage("Total point cannot be bigger than 15.")
            return
        }

        viewModelScope.launch {

            repository.deleteCache()

            val vehicle = Vehicle(
                name = vehicleName,
                durability = vehicleDurability,
                capacity = vehicleCapacity,
                velocity = vehicleVelocity,
            )

            val eus = vehicleVelocity * AppConst.EUS_FACTOR
            val ugs = vehicleCapacity * AppConst.UGS_FACTOR
            val ds = vehicleDurability * AppConst.DS_FACTOR

            preferencesManager.setUpPreferences(UGS = ugs, EUS = eus, DS = ds)
            repository.addOrUpdateVehicle(vehicle)
            createVehicleChannel.send(VehicleEvent.NavigateToDelivery)
        }


    }


    private fun showInValidInputMessage(msg: String) = viewModelScope.launch {
        createVehicleChannel.send(VehicleEvent.ShowInvalidInputMessage(msg))
    }


    sealed class VehicleEvent {
        data class ShowInvalidInputMessage(val msg: String) : VehicleEvent()
        object NavigateToDelivery : VehicleEvent()
    }
}