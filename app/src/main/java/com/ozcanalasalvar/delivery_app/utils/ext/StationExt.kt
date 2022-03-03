package com.ozcanalasalvar.delivery_app.utils.ext

import com.ozcanalasalvar.delivery_app.data.model.Station


fun Station.distance(station: Station): Int {
    val dst = kotlin.math.abs(this.coordinateX - station.coordinateX) + kotlin.math.abs(this.coordinateY - this.coordinateY)
    return dst.toInt()
}