package com.example.petslist.data.room_database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.petslist.data.model.Dog

@Dao
interface DogDao {
    @Insert
    suspend fun insert(dog: Dog)

    @Delete
    suspend fun delete(dog: Dog)

    @Query("DELETE FROM fav_dogs_table")
    suspend fun deleteAllDogs()

    @Query("SELECT * FROM fav_dogs_table")
    fun getAllDogs() : LiveData<List<Dog>>
}