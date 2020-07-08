package com.example.petslist.data


import androidx.lifecycle.LiveData
import com.example.petslist.data.api.DogDataSource
import com.example.petslist.data.api.TheDogApiService
import com.example.petslist.data.model.Dog
import com.example.petslist.data.room_database.DogDao



import javax.inject.Inject


class DogRepository @Inject constructor(private val dogDataSource: DogDataSource  , private val dogDao: DogDao) {

    suspend fun getNumberOfRandomDogs(limit:Int) = dogDataSource.getNumberOfRandomDogs(limit = limit)

    suspend fun insert(dog: Dog){ dogDao.insert(dog) }
    suspend fun delete(dog: Dog){ dogDao.delete(dog) }
    suspend fun deleteAllDogs(){ dogDao.deleteAllDogs() }
    fun getAllDogs() : LiveData<List<Dog>> { return dogDao.getAllDogs() }

}