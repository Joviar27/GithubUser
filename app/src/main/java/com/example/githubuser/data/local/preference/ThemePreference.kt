package com.example.githubuser.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreference(context: Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PREFS_KEY)
    private val dataStore = context.dataStore

    fun getThemeSetting() : Flow<Boolean> =
        dataStore.data.map {
            it[THEME_KEY] ?: false
        }

    suspend fun switchThemeSetting(){
        dataStore.edit {
            val current = it[THEME_KEY] ?: false
            it[THEME_KEY] = !current
        }
    }

    companion object{
        private const val PREFS_KEY = "theme_prefs"
        private val THEME_KEY = booleanPreferencesKey("themes_setting")

        @Volatile
        private var instance: ThemePreference? = null
        fun getInstance(context: Context): ThemePreference =
            instance ?: synchronized(this) {
                instance ?: ThemePreference(context.applicationContext)
            }.also { instance = it }
    }
}