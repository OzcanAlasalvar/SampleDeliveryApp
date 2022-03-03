package com.ozcanalasalvar.delivery_app.data.local.pref

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "PreferencesManager"

data class FilterPreferences(val UGS: Int, val EUS: Int, val DS: Int)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("delivery_preferences")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val ugs = preferences[PreferencesKeys.UGS_VALUE] ?: 0
            val eus = preferences[PreferencesKeys.EUS_VALUE] ?: 0
            val ds = preferences[PreferencesKeys.DS_VALUE] ?: 0
            FilterPreferences(ugs, eus, ds)
        }


    val dsFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.DS_VALUE] ?: 0
        }


    suspend fun setUpPreferences(UGS: Int, EUS: Int, DS: Int) {
        updateUGS(UGS)
        updateEUS(EUS)
        updateDS(DS)
    }

    suspend fun updateUGS(value: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.UGS_VALUE] = value
        }
    }

    suspend fun updateEUS(value: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EUS_VALUE] = value
        }
    }

    suspend fun updateDS(value: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DS_VALUE] = value
        }
    }


    private object PreferencesKeys {
        val UGS_VALUE = preferencesKey<Int>("ugs_value")
        val EUS_VALUE = preferencesKey<Int>("eus_value")
        val DS_VALUE = preferencesKey<Int>("ds_value")
    }
}