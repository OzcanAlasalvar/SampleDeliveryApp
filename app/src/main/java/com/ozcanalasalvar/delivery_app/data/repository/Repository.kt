package com.ozcanalasalvar.delivery_app.data.repository

import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.model.StationAndVehicle
import com.ozcanalasalvar.delivery_app.data.model.Vehicle
import com.ozcanalasalvar.delivery_app.data.utils.network.NetworkResult
import kotlinx.coroutines.flow.Flow


interface Repository {

    suspend fun fetchAndCacheStations(forceUpdate:Boolean): Flow<NetworkResult<List<Station>>>

    fun getStations(query: String): Flow<List<Station>>

    fun getFavoriteStations(): Flow<List<Station>>

    suspend fun updateStation(station: Station)

    suspend fun addOrUpdateVehicle(vehicle: Vehicle)

    fun getVehicle(): Flow<StationAndVehicle>

    suspend fun deleteCache()

    fun getStationByName(name: String): Flow<Station>
}