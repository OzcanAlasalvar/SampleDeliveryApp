package com.ozcanalasalvar.delivery_app.di

import android.content.Context
import androidx.room.Room
import com.ozcanalasalvar.delivery_app.data.local.db.AppDb
import com.ozcanalasalvar.delivery_app.data.utils.const.DatabaseConst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDb::class.java, DatabaseConst.DATABASE_NAME).build()

}