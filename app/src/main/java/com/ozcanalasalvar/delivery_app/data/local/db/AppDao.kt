package com.ozcanalasalvar.delivery_app.data.local.db

import androidx.room.*
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.model.StationAndVehicle
import com.ozcanalasalvar.delivery_app.data.model.Vehicle
import com.ozcanalasalvar.delivery_app.data.utils.const.DatabaseConst
import com.ozcanalasalvar.delivery_app.utils.AppConst
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    //fetch searched station
    @Query("SELECT * FROM Station WHERE name LIKE '%' || :stationQuery || '%'")
    fun getStations(stationQuery: String): Flow<List<Station>>

    //get all stations
    @Query("SELECT * FROM Station")
    suspend fun getAllStation(): List<Station>


    //get all stations
    @Query("SELECT * FROM Station WHERE name = :name")
    fun getStation(name: String): Flow<Station>

    //fetch favorite station
    @Query("SELECT * FROM Station WHERE isFavorite = 1")
    fun getFavoriteStations(): Flow<List<Station>>

    //update station
    @Update
    suspend fun updateStation(station: Station)

    //insert all stations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<Station>)

    @Query("DELETE FROM Station")
    fun deleteStationDB()

    @Query("DELETE FROM Vehicle")
    fun deleteVehicleDB()


    @Query("SELECT * FROM vehicle WHERE id = :vehicleId")
    fun getVehicle(vehicleId: Int = AppConst.VEHICLE_ID): Vehicle


    @Transaction
    @Query("SELECT * FROM Vehicle  WHERE id = :vehicleId")
    fun getVehicleWithStation(vehicleId: Int = AppConst.VEHICLE_ID): Flow<StationAndVehicle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: Vehicle)

    /**
     * Updating only currentStation
     * By order vehicleId
     */
    @Query("UPDATE Vehicle SET currentStationName=:stationName WHERE id = :vehicleId")
    fun updateVehicleStation(stationName: String, vehicleId: Int = AppConst.VEHICLE_ID)

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

}