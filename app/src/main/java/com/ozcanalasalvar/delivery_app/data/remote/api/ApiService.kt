package com.ozcanalasalvar.delivery_app.data.remote.api

import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.utils.SPACE_STATION_URL
import retrofit2.http.GET

interface ApiService {

    @GET(SPACE_STATION_URL)
    suspend fun getDeliveryStations(): List<Station>

}