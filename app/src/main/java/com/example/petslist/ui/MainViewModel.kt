package com.example.petslist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.petslist.data.MainRepository
import com.example.petslist.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : ViewModel(){
    fun getNumberOfRandomDogs(limit:Int, category_ids: Int?)=
        liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getNumberOfRandomDogs(limit,category_ids)))
        }catch (exception:Exception){
            emit(Resource.error(data = null,message = exception.message ?: "Got an error!"))
        }
    }
}