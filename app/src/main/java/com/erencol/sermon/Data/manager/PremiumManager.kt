package com.erencol.sermon.data.manager

import android.content.Context

class PremiumManager(context: Context) {
    private val sharedPreferencesManager = SharedPreferencesManager(context)

    var isPremium: Boolean
        get() = sharedPreferencesManager.getBoolean(PREMIUM_KEY, false)
        set(isPremium) = sharedPreferencesManager.putBoolean(PREMIUM_KEY, isPremium)

    companion object {
        private const val PREMIUM_KEY = "is_premium"
        
        @JvmStatic
        fun getInstance(context: Context): PremiumManager {
            return PremiumManager(context)
        }
    }
}
