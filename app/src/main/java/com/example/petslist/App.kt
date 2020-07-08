package com.example.petslist

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.petslist.di.ApiModule
import com.example.petslist.di.AppModule
import com.example.petslist.di.RoomModule
import com.example.petslist.ui.main.MainActivity
import com.example.petslist.ui.viewmodel.ViewModelFactory

import dagger.Component
import javax.inject.Singleton


// Definition of the Application graph
@Singleton
@Component(modules = [ApiModule::class, AppModule::class,RoomModule::class])
interface ApplicationComponent {
    //Activities
    fun inject(activity: MainActivity)


    //ViewModelFactory
    fun inject( vmFactory: ViewModelFactory)

    fun getAppContext():Context
}

// appComponent lives in the Application class to share its lifecycle
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