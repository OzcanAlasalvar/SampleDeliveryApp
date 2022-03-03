package com.ozcanalasalvar.delivery_app.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ozcanalasalvar.delivery_app.data.utils.const.DatabaseConst
import com.ozcanalasalvar.delivery_app.utils.AppConst
import com.squareup.moshi.Json

@Entity
data class Station(
    @PrimaryKey
    @Json(name = "name") val name: String,
    @Json(name = "coordinateX") val coordinateX: Double,
    @Json(name = "coordinateY") val coordinateY: Double,
    @Json(name = "capacity") val capacity: Int,
    @Json(name = "stock") var stock: Int,
    @Json(name = "need") var need: Int
) {
    //Station favorite status
    var isFavorite: Boolean = false

    //distance between current station and target station
    var deliveryDistance: Int = 0

    //distance to the world
    var distanceToWorld = 0
}

@Entity
data class Vehicle(
    @PrimaryKey
    val id: Int = AppConst.VEHICLE_ID,
    val name: String,
    val durability: Int,
    val capacity: Int,
    val velocity: Int,
    var durabilityValue: Int = AppConst.INITIAL_DAMAGE_CAPACITY,
    var currentStationName: String = AppConst.INITIAL_STATION
)


data class StationAndVehicle(
    @Embedded val vehicle: Vehicle,
    @Relation(
        parentColumn = "currentStationName",
        entityColumn = "name"
    )
    var station: Station?
)


