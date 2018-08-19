package com.facundolarrosa.androidcodetest3.rest

import android.content.Context
import com.facundolarrosa.androidcodetest3.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppRetrofit(context: Context) {

    private val retrofit: Retrofit
    val searchService: SearchService

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        searchService = retrofit.create(SearchService::class.java)
    }

}
