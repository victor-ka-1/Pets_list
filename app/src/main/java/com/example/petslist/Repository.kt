package com.example.petslist

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class Repository(
    baseUrl:String,
    apiKey:String,
    isDebugEnabled:Boolean
) {
    private val apiKeyHeader = "x-api-key"
    val retrofit: Retrofit

    init {
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            .apply {
                if (isDebugEnabled) level= HttpLoggingInterceptor.Level.BODY
                else level= HttpLoggingInterceptor.Level.NONE
            }
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(apiKeyHeader,apiKey)
                .build()
            chain.proceed(request)
        }.addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl( baseUrl )
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}

