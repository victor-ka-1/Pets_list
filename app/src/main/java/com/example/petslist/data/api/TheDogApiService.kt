package com.example.petslist.data.api

import com.example.petslist.data.model.Dog
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

enum class Order{
    ASC, DESC, RAND
}
interface TheDogApiService {
    @GET("images/search")
    suspend fun getNumberOfRandomDogs(
        @Query("limit") limit: Int,
        @Query("page") page:Int = 0,
        @Query("order") order : String = "ASC"

    ): Response<List<Dog>>
}