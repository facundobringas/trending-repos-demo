package com.facundolarrosa.androidcodetest3.retrofit

import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.model.Repo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubApiService {

    @GET("search/repositories")
    fun searchRepos(@Query("q") q: String = "created:>2012-06-01 language:kotlin stars:>=1000",
                    @Query("sort") sort: String = "stars",
                    @Query("order") order: String = "desc"): Call<ApiResult<Repo>>

    @Headers("Accept: application/vnd.github.3.raw")
    @GET("/repos/{owner}/{name}/contents/README.md")
    fun getReadMe(@Path("owner") owner: String, @Path("name") name: String): Call<ResponseBody>

    companion object {
        val instance: GithubApiService by lazy {
            RetrofitHolder.get().create(GithubApiService::class.java)
        }
    }
}