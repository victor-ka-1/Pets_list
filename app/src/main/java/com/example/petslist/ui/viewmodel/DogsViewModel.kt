package com.example.petslist.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.petslist.App
import com.example.petslist.data.DogRepository
import com.example.petslist.data.api.DogPagedDataSource
import com.example.petslist.data.api.Order
import com.example.petslist.data.model.Dog
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class DogsViewModel  ( private val dogRepository: DogRepository) : ViewModel(){

    val context:Context = App.appComponent.getAppContext()
    val apiService = App.appComponent.getApiService()

    private var order: Order? = null

    fun invalidatePagedList(){
        allDogsLiveData.value?.dataSource?.invalidate()
    }
    var allDogsLiveData :LiveData<PagedList<Dog>>

    private val config = PagedList.Config.Builder()
        .setPageSize(3)
        .setEnablePlaceholders(false)
        .build()
    init {
        allDogsLiveData =  initializedPagedListBuilder(config).build()
    }

    private fun  initializedPagedListBuilder(config : PagedList.Config) : LivePagedListBuilder<Int,Dog>{
        val dataSourceFactory: DataSource.Factory<Int, Dog> = object : DataSource.Factory<Int,Dog>(){
            override fun create(): DataSource<Int, Dog> {
                return DogPagedDataSource(
                    scope = viewModelScope,
                    apiService = apiService,
                    likedDogs = likedDogsLiveData,
                    order = order
                )
            }
        }
        return LivePagedListBuilder<Int, Dog>(dataSourceFactory, config)
    }
    fun setOrder(ord: Order){
        order= ord
        invalidatePagedList()
    }

    val likedDogsLiveData: LiveData<List<Dog>> = dogRepository.getAllDogs()
    fun addDogToLiked(dog: Dog){
        viewModelScope.launch {  dogRepository.insert(dog)}
    }
    fun removeDogFromLiked(dog: Dog){
        viewModelScope.launch {
            dogRepository.delete(dog)
        }
    }
    fun clearLikedDogsList(){
        viewModelScope.launch { dogRepository.deleteAllDogs()}
    }



    fun downloadImage(imageURL: String) {
        //1)USE To save in public download directory like"/storage/emulated/0/Download/PetsApp"
        val dirPath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+"PetsApp"
        val dir = File(dirPath)
        /*2)OR use this to save in app directory like "/storage/emulated/0/Android/data/com.example.petslist/files/Download"*/
        // val dir = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!

        val fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1)
        Glide.with(context)
            .load(imageURL)
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared( placeholder: Drawable?) {}
                override fun onLoadFailed( errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(context, "Failed to Download Image. Please try again later", Toast.LENGTH_SHORT).show()
                }
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    Toast.makeText(context, "Saving Image...", Toast.LENGTH_SHORT).show()
                    saveImage(bitmap, dir, fileName)
                }
            })
    }
    private fun saveImage(image: Bitmap, storageDir: File, imageFileName: String) {

        var fOut: OutputStream? = null

        val isDirCreated =  if (!storageDir.exists()) { storageDir.mkdir() } else true

        if (isDirCreated) {
            val imageFile = File(storageDir, imageFileName)
            try {
                fOut  = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Error while saving image!", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }finally {
                fOut?.close()
            }
        } else {
            Toast.makeText(context, "Failed to make folder!", Toast.LENGTH_SHORT).show()
        }
    }
}