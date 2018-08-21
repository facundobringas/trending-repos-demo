package com.facundolarrosa.androidcodetest3.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val API_URL = "https://api.github.com/"

object RetrofitHolder {

    private var instance: Retrofit? = null

    @Synchronized
    private fun create() {
        if (instance == null) {
            instance = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }

    fun get(): Retrofit {
        if (instance == null) create()
        return instance!!
    }
}