package com.github.keyrillanskiy.city_mobil_test

import android.app.Application
import com.github.keyrillanskiy.city_mobil_test.data.network.initConnectivityManager

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initConnectivityManager(this)
    }

}