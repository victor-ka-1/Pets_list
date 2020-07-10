package com.example.petslist

import android.app.Application
import android.content.Context
import com.example.petslist.data.api.TheDogApiService
import com.example.petslist.di.ApiModule
import com.example.petslist.di.AppModule
import com.example.petslist.di.RoomModule
import com.example.petslist.ui.main.MainActivity
import com.example.petslist.ui.viewmodel.ViewModelFactory
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApiModule::class, AppModule::class,RoomModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject( vmFactory: ViewModelFactory)

    fun getAppContext():Context
    fun getApiService():TheDogApiService
}

class App : Application(){
    companion object {
        lateinit var appComponent: ApplicationComponent
    }
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .builder()
            .roomModule( RoomModule(context = applicationContext))
            .build()
    }
}