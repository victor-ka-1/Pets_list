package com.example.petslist.di

import com.example.petslist.data.DogRepository
import com.example.petslist.data.api.DogDataSource
import com.example.petslist.data.api.TheDogApiService
import com.example.petslist.data.room_database.DogDao
import com.example.petslist.ui.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
 class AppModule {
    @Singleton
    @Provides
    fun provideDogRepository(dogDataSource: DogDataSource , dogDao: DogDao):DogRepository{
        return DogRepository(dogDataSource = dogDataSource , dogDao = dogDao )
    }

    @Singleton
    @Provides
    fun provideDogDataSource(apiService: TheDogApiService):DogDataSource{
        return DogDataSource(api = apiService)
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(repository: DogRepository) :ViewModelFactory{
        return ViewModelFactory(repository)
    }

}