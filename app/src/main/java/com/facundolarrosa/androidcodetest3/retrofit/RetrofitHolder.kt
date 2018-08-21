package com.facundolarrosa.androidcodetest3.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson



private const val API_URL = "https://api.github.com/"

object RetrofitHolder {

    private var instance: Retrofit? = null

    @Synchronized
    private fun create() {
        if (instance == null) {
            val gson = GsonBuilder().setLenient().create()
            instance = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }
    }

    fun get(): Retrofit {
        if (instance == null) create()
        return instance!!
    }
}