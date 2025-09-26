package com.example.engvocab.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemePreference(private val context: Context) {
    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_enabled")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Đọc giá trị, mặc định là false (Light Theme)
            preferences[DARK_THEME_KEY] ?: false
        }

    // Ghi trạng thái theme
    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { settings ->
            settings[DARK_THEME_KEY] = isDark
        }
    }

}