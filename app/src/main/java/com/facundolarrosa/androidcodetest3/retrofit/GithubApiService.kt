package com.facundolarrosa.androidcodetest3.retrofit

import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.model.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface GithubApiService {

    @GET("search/repositories")
    fun searchRepos(@Query("q") q: String = "created:>2018-08-01 topic:android language:java language:kotlin stars:>=1 forks:>=1",
                    @Query("sort") sort: String = "stars",
                    @Query("order") order: String = "desc"): Call<ApiResult<Repo>>

    companion object {
        val instance: GithubApiService by lazy {
            RetrofitHolder.get().create(GithubApiService::class.java)
        }
    }
}