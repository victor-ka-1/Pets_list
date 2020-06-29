package com.example.petslist.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petslist.data.MainRepository
import com.example.petslist.data.api.ApiHelper
import com.example.petslist.ui.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory (private val apiHelper: ApiHelper) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if( modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}