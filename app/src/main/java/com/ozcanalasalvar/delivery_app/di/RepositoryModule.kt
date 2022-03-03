package com.ozcanalasalvar.delivery_app.di

import android.content.Context
import android.content.SharedPreferences
import com.ozcanalasalvar.delivery_app.data.local.db.AppDb
import com.ozcanalasalvar.delivery_app.data.remote.api.ApiService
import com.ozcanalasalvar.delivery_app.data.repository.Repository
import com.ozcanalasalvar.delivery_app.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService, db: AppDb): Repository =
        RepositoryImpl(apiService, db)

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}