package com.example.petslist.data.api

import com.example.petslist.data.model.Dog
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TheDogApi {

    @GET("images/search")
    suspend fun getNumberOfRandomDogs(
        @Query("limit") limit: Int
//        ,
//        @Query("category_ids") category_ids: Int?
    ): List<Dog>
}

object RetrofitBuilder{
    private const val BASE_URL= "https://api.thedogapi.com/v1/"
    private fun getRetrofit() :Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val theDogApi: TheDogApi = getRetrofit().create(TheDogApi::class.java)
}

class ApiHelper(private val theDogApi:TheDogApi){
    suspend fun getNumberOfRandomDogs(limit: Int,category_ids: Int?) =
        theDogApi.getNumberOfRandomDogs(limit = limit/*, category_ids = category_ids*/)

}