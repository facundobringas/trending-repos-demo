package com.facundolarrosa.androidcodetest3

import android.app.Application
import com.facundolarrosa.androidcodetest3.rest.AppRetrofit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppRetrofit(this)
    }
}