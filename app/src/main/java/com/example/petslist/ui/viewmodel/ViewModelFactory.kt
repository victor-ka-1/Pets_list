package com.example.petslist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petslist.App
import com.example.petslist.data.DogRepository
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: DogRepository) :ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if( modelClass.isAssignableFrom(DogsViewModel::class.java)){
            return DogsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}