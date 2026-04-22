package com.erencol.sermon

import android.app.Application
import com.google.android.gms.ads.MobileAds

class SermonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
    }
}
