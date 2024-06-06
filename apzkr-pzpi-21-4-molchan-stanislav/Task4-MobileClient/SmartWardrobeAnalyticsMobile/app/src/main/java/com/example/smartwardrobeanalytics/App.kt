package com.example.smartwardrobeanalytics

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Config.loadConfig(this)
    }
}