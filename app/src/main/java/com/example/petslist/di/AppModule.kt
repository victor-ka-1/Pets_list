package com.example.petslist.di

import com.example.petslist.data.DogRepository
import com.example.petslist.data.room_database.DogDao
import com.example.petslist.ui.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
 class AppModule {
    @Singleton
    @Provides
    fun provideDogRepository( dogDao: DogDao):DogRepository{
        return DogRepository(dogDao = dogDao )
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(repository: DogRepository) :ViewModelFactory{
        return ViewModelFactory(repository)
    }
}