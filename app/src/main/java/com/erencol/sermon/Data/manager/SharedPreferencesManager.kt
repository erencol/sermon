package com.erencol.sermon.data.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {
    val FONT_KEY = "font_key"
    val DEFAULT_FONT_SIZE = 4
    companion object {
        private const val PREFS_NAME = "SermonPrefs"
        private var instance: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager {
            if (instance == null) {
                instance = SharedPreferencesManager(context.applicationContext)
            }
            return instance!!
        }
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ------------------ Put methods ------------------
    fun putString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    fun putFontSize(value: Int) {
        prefs.edit { putInt(FONT_KEY, value) }
    }

    fun putIntForKey(forKey: String, value: Int) {
        prefs.edit { putInt(forKey, value) }
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
    }

    // ------------------ Get methods ------------------
    fun getString(key: String, default: String? = null): String? {
        return prefs.getString(key, default)
    }

    fun getIntForKey(key: String, default: Int = 0): Int {
        return prefs.getInt(key, default)
    }

    fun getFontSize(default: Int = 0): Int {
        return prefs.getInt(FONT_KEY, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(key, default)
    }

    // ------------------ Remove / Clear ------------------
    fun remove(key: String) {
        prefs.edit { remove(key) }
    }

    fun clear() {
        prefs.edit { clear() }
    }
}