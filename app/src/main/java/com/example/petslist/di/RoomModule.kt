package com.example.petslist.di

import android.content.Context
import androidx.room.Room
import com.example.petslist.data.room_database.DogDao
import com.example.petslist.data.room_database.DogDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
 class RoomModule(private val context: Context) {
 @Provides
 fun provideApplicationContext(): Context{
  return context
 }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): DogDataBase {
        return Room.databaseBuilder(context, DogDataBase::class.java,"dogDataBase" ).build()
    }

    @Singleton
    @Provides
    fun provideDogDao( dataBase: DogDataBase): DogDao {
        return dataBase.dogDao()
    }

}