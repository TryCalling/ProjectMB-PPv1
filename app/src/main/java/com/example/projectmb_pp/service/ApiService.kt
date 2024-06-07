package com.example.projectmb_pp.service

import com.example.projectmb_pp.model.Property
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//private const val BASE_URL = "https://fakestoreapi.com/products/"
//private const val BASE_URL = "https://raw.githubusercontent.com/bikashthapa01/myvideos-android-app/master/data.json"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()

interface ApiService{

    @GET("/movie/index")
    fun getAllData(): Call<List<Property>>

}

object Api {
    private const val BASE_URL = "http://13.228.32.137:8888"

    val retrofitService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}