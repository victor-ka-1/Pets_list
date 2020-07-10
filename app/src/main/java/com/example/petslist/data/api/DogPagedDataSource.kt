package com.example.petslist.data.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PositionalDataSource
import com.example.petslist.data.model.Dog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class DogPagedDataSource (private val apiService: TheDogApiService,
                          private val scope: CoroutineScope,
                          private val likedDogs:LiveData<List<Dog>>,
                          private val order: Order?) : PageKeyedDataSource<Int, Dog>(){

    private fun getOrder(order: Order?): String {
        order?.let { return order.name}
        return Order.ASC.name
    }
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Dog>) {
        scope.launch {
                try {
                    val response = apiService.getNumberOfRandomDogs(limit = params.requestedLoadSize, order = getOrder(order))
                    when{
                        response.isSuccessful -> {
                            val list = response.body()
                            likedDogs.value?.let {
                                for (likedDog in likedDogs.value!!) {
                                    list?.forEach {
                                        if (it.id == likedDog.id)
                                            it.liked = true
                                    }
                                }
                            }
                            val page =  response.headers().get("Pagination-Page")?.toInt()
                            val previousPage = page?.let { if(page == 0) null else page-3 }
                            val maxPage = response.headers().get("Pagination-Count")?.toInt()
                            val nextPage = maxPage?.let {
                                page?.let { if(maxPage > page) page.plus(3) else null }
                            }
                            callback.onResult( list?: listOf(), previousPage, nextPage  )
                        }
                    }
                }catch (exception : Exception){
                    Log.e("DogPagedDataSource", "Failed to fetch data! exception: $exception")
                }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Dog>) {
        scope.launch {
            try {
                val response =
                    apiService.getNumberOfRandomDogs(limit = params.requestedLoadSize, page = params.key , order = getOrder(order))
                when{
                    response.isSuccessful -> {
                        val list = response.body()
                        likedDogs.value?.let {
                            for (likedDog in likedDogs.value!!) {
                                list?.forEach {
                                    if (it.id == likedDog.id)
                                        it.liked = true
                                }
                            }
                        }
                        val page =  response.headers().get("Pagination-Page")?.toInt()
                        val maxPage = response.headers().get("Pagination-Count")?.toInt()
                        val nextPage = maxPage?.let {
                            page?.let { if(maxPage > page) page.plus(1) else null }
                        }
                        callback.onResult(list ?: listOf(), nextPage)
                    }
                }
            }catch (exception: Exception){
                Log.e("DogPagedDataSource", "Failed to fetch data! exception: $exception")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Dog>) {
        scope.launch {
            try {
                val response =
                    apiService.getNumberOfRandomDogs(limit = params.requestedLoadSize, page = params.key)
                when{
                    response.isSuccessful -> {
                        val list = response.body()
                        likedDogs.value?.let {
                            for (likedDog in likedDogs.value!!) {
                                list?.forEach {
                                    if (it.id == likedDog.id)
                                        it.liked = true
                                }
                            }
                        }
                        val page =  response.headers().get("Pagination-Page")?.toInt()
                        val previousPage = page?.let { if(page == 0) null else page-1 }
                        callback.onResult(list ?: listOf(),previousPage )
                    }
                }
            }catch (exception: Exception){
                Log.e("DogPagedDataSource", "Failed to fetch data! exception: $exception")
            }

        }
    }

    override fun invalidate() {
        super.invalidate()
     //   scope.cancel()
    }
}

//
//class DogPositionalDataSource (private val apiService: TheDogApiService,
//                               private val scope: CoroutineScope,
//                               private val likedDogs:LiveData<List<Dog>>) : PositionalDataSource<Dog>(){
//    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Dog>) {
//        scope.launch {
//            try{
//                val position = if (params.startPosition != 0)( params.startPosition -1) else 0
//                val response =
//                    apiService.getNumberOfRandomDogs(limit = params.loadSize, page = position )
//                when{
//                    response.isSuccessful -> {
//                        val list = response.body()
//                        likedDogs.value?.let {
//                            for (likedDog in likedDogs.value!!) {
//                                list?.forEach {
//                                    if (it.id == likedDog.id)
//                                        it.liked = true
//                                }
//                            }
//                        }
//                        callback.onResult( list?: listOf())
//                    }
//                }
//            }catch (exception : Exception){
//                Log.e("DogPagedDataSource", "Failed to fetch data! exception: $exception")
//            }
//        }
//    }
//
//    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Dog>) {
//        scope.launch {
//            try {
//                val response =
//                    apiService.getNumberOfRandomDogs(limit = params.requestedLoadSize, page = params.requestedStartPosition)
//                when{
//                    response.isSuccessful -> {
//                        val list = response.body()
//                        likedDogs.value?.let {
//                            for (likedDog in likedDogs.value!!) {
//                                list?.forEach {
//                                    if (it.id == likedDog.id)
//                                        it.liked = true
//                                }
//                            }
//                        }
//                        val page =  response.headers().get("Pagination-Page")?.toInt()
//                        val maxPage = response.headers().get("Pagination-Count")?.toInt()
//                        callback.onResult( list?: listOf(), page!!, maxPage!!)
//                    }
//                }
//            }catch (exception : Exception){
//                Log.e("DogPagedDataSource", "Failed to fetch data! exception: $exception")
//            }
//        }
//    }
//}

