package com.ozcanalasalvar.delivery_app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ozcanalasalvar.delivery_app.data.model.Station
import com.ozcanalasalvar.delivery_app.data.model.Vehicle
import com.ozcanalasalvar.delivery_app.data.utils.const.DatabaseConst

@Database(
    entities = [Station::class, Vehicle::class],
    version = DatabaseConst.DATABASE_VERSION, exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun getDao(): AppDao
}