package com.example.petslist.data.api

import androidx.paging.PositionalDataSource
import com.example.petslist.data.model.Dog
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class DogDataSource(private val api:TheDogApiService) {
    suspend fun getNumberOfRandomDogs(limit: Int) =api.getNumberOfRandomDogs(limit= limit)
}

class DogPositionalDataSource : PositionalDataSource<Dog>(){
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Dog>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Dog>) {

    }

}