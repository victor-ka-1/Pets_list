package com.example.petslist.data

import com.example.petslist.data.api.ApiHelper


class MainRepository(private val apiHelper: ApiHelper){
    suspend fun getNumberOfRandomDogs(limit:Int, category_ids: Int?) = apiHelper.getNumberOfRandomDogs(limit,category_ids)
}