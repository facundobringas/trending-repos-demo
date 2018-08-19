package com.facundolarrosa.androidcodetest3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.facundolarrosa.androidcodetest3.rest.AppRetrofit
import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.model.Repo
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val call = AppRetrofit(this).searchService.search()
        call.enqueue(object: Callback<ApiResult<Repo>?> {
            override fun onResponse(call: Call<ApiResult<Repo>?>?,
                                    response: Response<ApiResult<Repo>?>?) {

                response?.body()?.let {
                    val repos: List<Repo> = it.items
                    configureList(repos)
                }

            }

            override fun onFailure(call: Call<ApiResult<Repo>?>?,
                                   t: Throwable?) {

            }
        })
    }

    private fun configureList(repos : List<Repo>){
        tv_hello.text = repos.toString()
        main_progress_bar.visibility = View.GONE
    }
}
