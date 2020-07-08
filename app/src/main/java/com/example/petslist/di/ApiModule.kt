package com.example.petslist.di

import com.example.petslist.data.api.TheDogApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule{

    @Singleton
    @Provides
    fun provideOKHttpClient():OkHttpClient{
        val loggingInterceptor = HttpLoggingInterceptor()
        if (true) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .readTimeout(1200,TimeUnit.SECONDS)
            .connectTimeout(1200,TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideGSON():GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient):Retrofit{
        val BASE_URL= "https://api.thedogapi.com/v1/"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideTheDogApi(retrofit: Retrofit): TheDogApiService {
        return retrofit.create(TheDogApiService::class.java)
    }
}


