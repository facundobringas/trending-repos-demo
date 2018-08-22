package com.facundolarrosa.androidcodetest3.intentservice

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v4.content.LocalBroadcastManager
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.model.Repo
import com.facundolarrosa.androidcodetest3.retrofit.GithubApiService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val ACTION_SEARCH_REPOS = "SEARCH_REPOS"
private const val ACTION_GET_README = "GET_README"

private const val REPO_OWNER = "REPO_OWNER"
private const val REPO_NAME = "REPO_NAME"

private const val UNPROCESSABLE_ENTITY = 422

class HttpIntentService : IntentService("HttpIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_SEARCH_REPOS -> {
                handleSearchRepos()
            }
            ACTION_GET_README -> {
                val repoOwner = intent.getStringExtra(REPO_OWNER)
                val repoName = intent.getStringExtra(REPO_NAME)
                handleGetReadMe(repoOwner, repoName)
            }
        }
    }

    private fun handleSearchRepos() {
        if(!isNetworkAvailable()){
            onSeacrhReposError(getString(R.string.no_network_available))
            return
        }

        val call = GithubApiService.instance.searchRepos()
        call.enqueue(object: Callback<ApiResult<Repo>?> {
            override fun onResponse(call: Call<ApiResult<Repo>?>?,
                                    response: Response<ApiResult<Repo>?>?) {

                if(response?.isSuccessful!!){
                    response.body()?.let {
                        val repos: List<Repo> = it.items
                        onSearchReposSuccess(repos)
                    }?: run {
                        onSeacrhReposError(this@HttpIntentService.getString(R.string.empty_response, this@HttpIntentService.getString(R.string.our_bad )))
                    }
                }else if (response.code() == UNPROCESSABLE_ENTITY){
                    val jObjError = JSONObject(response.errorBody()?.string())
                    onSeacrhReposError(this@HttpIntentService.getString(R.string.unprocessable_entity, this@HttpIntentService.getString(R.string.our_bad ), jObjError.getString("message")))
                }else{
                    onSeacrhReposError(this@HttpIntentService.getString(R.string.http_error, this@HttpIntentService.getString(R.string.our_bad ), response.code()))
                }

            }

            override fun onFailure(call: Call<ApiResult<Repo>?>?,
                                   t: Throwable?) {
                when(t) {
                    is SocketTimeoutException -> onSeacrhReposError(this@HttpIntentService.getString(R.string.socket_timeout))
                    is UnknownHostException -> onSeacrhReposError(this@HttpIntentService.getString(R.string.unknown_host))
                    else -> onSeacrhReposError(this@HttpIntentService.getString(R.string.conversion_error, this@HttpIntentService.getString(R.string.our_bad ), t?.message))
                }
            }
        })
    }

    fun onSearchReposSuccess(repos: List<Repo>){
        val successBroadCastIntent = Intent(SEARCH_REPOS_SUCCESS)
        successBroadCastIntent.putParcelableArrayListExtra(SEARCH_REPOS_RESULT, repos as ArrayList<Repo>)
        LocalBroadcastManager.getInstance(this).sendBroadcast(successBroadCastIntent)

    }

    fun onSeacrhReposError(message:String){
        val errorBroadCastIntent = Intent(SEARCH_REPOS_ERROR)
        errorBroadCastIntent.putExtra(SEARCH_REPOS_ERROR_MESSAGE, message)
        LocalBroadcastManager.getInstance(this).sendBroadcast(errorBroadCastIntent)
    }

    private fun handleGetReadMe(owner: String, name: String) {
        if(!isNetworkAvailable()){
            return
        }

        val call = GithubApiService.instance.getReadMe(owner, name)
        call.enqueue(object: Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?,
                                    response: Response<ResponseBody?>?) {

                if(response?.isSuccessful!!){
                    response.body()?.let {
                        val readMe: String? = response.body()?.string()
                        onGetReadMeSuccess(readMe!!)
                    }?: run {
                        onGetReadmeError()
                    }
                }else{
                    onGetReadmeError()
                }

            }

            override fun onFailure(call: Call<ResponseBody?>?,
                                   t: Throwable?) {
                onGetReadmeError()
            }
        })
    }

    private fun onGetReadmeError() {
        val successBroadCastIntent = Intent(GET_README_ERROR)
        LocalBroadcastManager.getInstance(this).sendBroadcast(successBroadCastIntent)
    }

    private fun onGetReadMeSuccess(readMe: String) {
        val successBroadCastIntent = Intent(GET_README_SUCCESS)
        successBroadCastIntent.putExtra(GET_README_RESULT, readMe)
        LocalBroadcastManager.getInstance(this).sendBroadcast(successBroadCastIntent)
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    companion object {

        @JvmStatic
        fun startSearchRepos(context: Context) {
            val intent = Intent(context, HttpIntentService::class.java).apply {
                action = ACTION_SEARCH_REPOS
            }
            context.startService(intent)
        }

        @JvmStatic
        fun startGetReadMe(context: Context, repoOwner: String, repoName: String) {
            val intent = Intent(context, HttpIntentService::class.java).apply {
                action = ACTION_GET_README
                putExtra(REPO_OWNER, repoOwner)
                putExtra(REPO_NAME, repoName)
            }
            context.startService(intent)
        }

        @JvmStatic val SEARCH_REPOS_RESULT = "SEARCH_REPOS_RESULT"
        @JvmStatic val SEARCH_REPOS_SUCCESS = "SEARCH_REPOS_SUCCES"
        @JvmStatic val SEARCH_REPOS_ERROR = "SEARCH_REPOS_ERROR"
        @JvmStatic val SEARCH_REPOS_ERROR_MESSAGE = "SEARCH_REPOS_ERROR_MESSAGE"

        @JvmStatic val GET_README_RESULT = "GET_README_RESULT"
        @JvmStatic val GET_README_SUCCESS = "GET_README_SUCCES"
        @JvmStatic val GET_README_ERROR = "GET_README_ERROR"
    }
}
