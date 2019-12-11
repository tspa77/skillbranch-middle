package ru.skillbranch.gameofthrones

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.skillbranch.gameofthrones.di.applicationModule

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(applicationModule))
    }
}