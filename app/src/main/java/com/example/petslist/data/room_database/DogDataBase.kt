package com.example.petslist.data.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petslist.data.model.Dog


@Database(entities = [Dog::class],version = 1,exportSchema = false)
abstract class DogDataBase : RoomDatabase(){
    abstract fun dogDao():DogDao
}