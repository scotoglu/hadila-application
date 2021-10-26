package com.scoto.hadila.helper

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.scoto.hadila.util.AUTH_PREFERENCES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AUTH_PREFERENCES)

class DataStoreManager @Inject constructor(
    val context: Context
) {

    suspend fun saveAuthID(value: String) {
        Log.d(TAG, "saveAuthID: Called")
        context.dataStore.edit {
            it[AUTH_KEY] = value
        }
    }

    val authIDFlow: Flow<String> = context.dataStore.data.map { preference ->
        Log.d(TAG, "AuthIdFlow : ${preference[AUTH_KEY]}")
        preference[AUTH_KEY] ?: ""
    }

    suspend fun removeAuthID() {
        Log.d(TAG, "removeAuthID: ")
        context.dataStore.edit {
            it.remove(AUTH_KEY)
        }
    }


    suspend fun saveRememberMe(value: Boolean) {
        context.dataStore.edit {
            it[REMEMBER_ME] = value
        }
    }

    val rememberMeFlow: Flow<Boolean?> =
        context.dataStore.data.map { preference -> preference[REMEMBER_ME] }

    suspend fun removeRememberMe() {
        context.dataStore.edit {
            it.remove(REMEMBER_ME)
        }
    }

    companion object {
        private const val TAG = "DataStoreManager"
        val AUTH_KEY = stringPreferencesKey("uid")
        val REMEMBER_ME = booleanPreferencesKey("remember_me")
    }
}