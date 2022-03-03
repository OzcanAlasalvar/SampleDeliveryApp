package com.ozcanalasalvar.delivery_app.di

import com.ozcanalasalvar.delivery_app.BuildConfig
import com.ozcanalasalvar.delivery_app.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpsClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .build()


    @Singleton
    @Provides
    fun provideGSON(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}