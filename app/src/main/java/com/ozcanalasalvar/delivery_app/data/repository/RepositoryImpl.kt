package com.ozcanalasalvar.delivery_app.data.repository

import androidx.room.withTransaction
import com.ozcanalasalvar.delivery_app.data.local.db.AppDb
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.model.StationAndVehicle
import com.ozcanalasalvar.delivery_app.data.model.Vehicle
import com.ozcanalasalvar.delivery_app.data.remote.api.ApiService
import com.ozcanalasalvar.delivery_app.data.utils.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDb
) : Repository {

    private val dao = db.getDao()

    override suspend fun fetchAndCacheStations(forceUpdate:Boolean) = networkBoundResource(
        query = {
            dao.getAllStation()
        },
        fetch = {
            apiService.getDeliveryStations()
        },
        saveFetchResult = { data ->
            db.withTransaction {
                dao.insertStations(data)
                if (data.isNotEmpty()) {
                    dao.updateVehicleStation(data[0].name)
                }

            }
        }, shouldFetch = {
            it.isNullOrEmpty() ||forceUpdate
        }
    )

    override fun getStations(query: String) = dao.getStations(query)

    override fun getStationByName(name: String) = dao.getStation(name)

    override fun getFavoriteStations() = dao.getFavoriteStations()

    override suspend fun updateStation(station: Station) = dao.updateStation(station)

    override suspend fun deleteCache() {
        db.withTransaction {
            dao.deleteStationDB()
            dao.deleteVehicleDB()
        }
    }

    override suspend fun addOrUpdateVehicle(vehicle: Vehicle) = dao.insertVehicle(vehicle)

    override fun getVehicle(): Flow<StationAndVehicle> = dao.getVehicleWithStation()

}