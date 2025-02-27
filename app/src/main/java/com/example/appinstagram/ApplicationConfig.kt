package com.example.appinstagram

import android.app.Application
import com.example.appinstagram.di.RepositoryModule
import com.example.appinstagram.di.ViewModelModule
import com.example.appinstagram.di.apiModule
import com.example.appinstagram.lib_base.utils.BaseContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.tencent.mmkv.MMKV

class ApplicationConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        BaseContext.instance.init(this)
        MMKV.initialize(this)
        startKoin {
            androidContext(this@ApplicationConfig)
            modules(listOf(apiModule, RepositoryModule, ViewModelModule))
        }
    }
}