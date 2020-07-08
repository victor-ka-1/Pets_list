package com.example.petslist.data.api

import com.example.petslist.data.model.Dog
import retrofit2.http.GET
import retrofit2.http.Query

interface TheDogApiService {
    @GET("images/search")
    suspend fun getNumberOfRandomDogs(
        @Query("limit") limit: Int,
        @Query("order") order : String = "DESC"
//        ,
//        @Query("page") page:Int,
//        @Query("category_ids") category_ids: Int?
    ): List<Dog>
}