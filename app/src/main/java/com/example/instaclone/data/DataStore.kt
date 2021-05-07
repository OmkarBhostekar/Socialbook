package com.example.instaclone.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.first

object DataStore {
    suspend fun saveStringToDS(key: String, value: String,dataStore: DataStore<Preferences>) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun saveIntToDS(key: String, value: Int,dataStore: DataStore<Preferences>) {
        val dataStoreKey = preferencesKey<Int>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun saveBooleeanToDS(key: String, value: Boolean,dataStore: DataStore<Preferences>) {
        val dataStoreKey = preferencesKey<Boolean>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = value
        }
    }
    suspend fun readStringFromDS(key: String,dataStore: DataStore<Preferences>) : String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
    suspend fun readIntFromDS(key: String,dataStore: DataStore<Preferences>) : Int? {
        val dataStoreKey = preferencesKey<Int>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
    suspend fun readBooleanFromDS(key: String,dataStore: DataStore<Preferences>) : Boolean {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey] ?: false
    }
}