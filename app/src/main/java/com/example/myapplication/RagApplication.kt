package com.example.myapplication

import android.app.Application
import com.example.myapplication.services.AppContainer
import com.example.myapplication.services.DefaultAppContainer

class RagApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}
