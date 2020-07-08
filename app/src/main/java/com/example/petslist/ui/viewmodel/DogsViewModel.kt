package com.example.petslist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.petslist.data.DogRepository
import com.example.petslist.data.model.Dog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DogsViewModel  ( private val dogRepository: DogRepository) : ViewModel(){

    private val allDogsLiveData: MutableLiveData<List<Dog>> = MutableLiveData()
    init {
        allDogsLiveData.value = ArrayList()
    }
    fun getAllDogsLiveData():LiveData<List<Dog>> = allDogsLiveData


    fun getNumberOfRandomDogs(limit:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newList = dogRepository.getNumberOfRandomDogs(limit = limit)
                val list =  allDogsLiveData.value as MutableList<Dog>
                list.addAll(newList)
                allDogsLiveData.postValue(list)
                allDogsLiveData.value = list
            } catch (exception: Exception) {
                Log.e(this@DogsViewModel::class.java.simpleName, "  Error while getting data")
            }
        }
    }

    val favDogsLiveData: LiveData<List<Dog>> = dogRepository.getAllDogs()

    fun addDogToFav(dog: Dog){
        viewModelScope.launch {  dogRepository.insert(dog)}
    }
    fun removeDogFromFav(dog: Dog){
        viewModelScope.launch {  dogRepository.delete(dog)}
    }
    fun removeAllDogsFromFav(){
        viewModelScope.launch { dogRepository.deleteAllDogs() }
    }

}