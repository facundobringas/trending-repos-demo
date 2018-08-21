package com.facundolarrosa.androidcodetest3.activity

import android.app.FragmentManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.facundolarrosa.androidcodetest3.ErrorFragment
import com.facundolarrosa.androidcodetest3.NoResultsFragment
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.repo.RepoDetailFragment
import com.facundolarrosa.androidcodetest3.model.Repo
import com.facundolarrosa.androidcodetest3.repo.RepoRecyclerViewAdapter
import com.facundolarrosa.androidcodetest3.rest.AppRetrofit
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.repo_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val UNPROCESSABLE_ENTITY = 422
private const val ERROR = "errorFragment"

class MainActivity : AppCompatActivity(){

    private lateinit var repoAdapter: RepoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = getString(R.string.main_activity_title)

        val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val repo = v.tag as Repo
            onRepoItemClick(repo)
        }

        repoAdapter = RepoRecyclerViewAdapter(ArrayList(), onClickListener)

        if (rv_repo_list is RecyclerView) {
            with(rv_repo_list) {
                adapter = repoAdapter
            }
        }

        fetch()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_refresh -> fetch()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0 ){
            if(supportFragmentManager.backStackEntryCount == 1){
                if(supportFragmentManager.getBackStackEntryAt(0).name == ERROR){
                    super.onBackPressed()
                }else {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    title = getString(R.string.main_activity_title)
                }
            }
        }
        super.onBackPressed()
    }

    fun fetch() {
        if(!isNetworkAvailable()){
            onRepoListError(getString(R.string.no_network_available))
            return
        }

        supportFragmentManager.popBackStack(ERROR, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        main_progress_bar.visibility = View.VISIBLE

        val call = AppRetrofit(this).searchService.search()
        call.enqueue(object: Callback<ApiResult<Repo>?> {
            override fun onResponse(call: Call<ApiResult<Repo>?>?,
                                    response: Response<ApiResult<Repo>?>?) {

                if(response?.isSuccessful!!){
                    response?.body()?.let {
                        val repos: List<Repo> = it.items
                        loadRepos(repos)
                    }?: run {
                        onRepoListError(this@MainActivity.getString(R.string.empty_response, this@MainActivity.getString(R.string.our_bad )))
                    }
                }else if (response?.code() == UNPROCESSABLE_ENTITY){
                    val jObjError = JSONObject(response?.errorBody()?.string())
                    onRepoListError(this@MainActivity.getString(R.string.unprocessable_entity, this@MainActivity.getString(R.string.our_bad ), jObjError.getString("message")))
                }else{
                    onRepoListError(this@MainActivity.getString(R.string.http_error, this@MainActivity.getString(R.string.our_bad ), response?.code()))
                }

            }

            override fun onFailure(call: Call<ApiResult<Repo>?>?,
                                   t: Throwable?) {
                when(t) {
                    is SocketTimeoutException -> onRepoListError(this@MainActivity.getString(R.string.socket_timeout))
                    is UnknownHostException -> onRepoListError(this@MainActivity.getString(R.string.unknown_host))
                    else -> onRepoListError(this@MainActivity.getString(R.string.conversion_error, this@MainActivity.getString(R.string.our_bad ), t?.message))
                }
            }
        })
    }

    private fun loadRepos(repos : List<Repo>){
        if(repos.size > 0) {
            repoAdapter.mRepos = repos
            repoAdapter.notifyDataSetChanged()
        }else{
            val fragment = NoResultsFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(ERROR)
                    .add(R.id.main_container, fragment)
                    .commit()
        }
        main_progress_bar.visibility = View.GONE
    }

    fun onRepoListError(error: String?) {
        main_progress_bar.visibility = View.GONE
        val fragment = ErrorFragment.newInstance(error)
        supportFragmentManager
                .beginTransaction()
                .addToBackStack(ERROR)
                .add(R.id.main_container, fragment)
                .commit()
    }

    fun onRepoItemClick(repo: Repo) {
        val fragment = RepoDetailFragment.newInstance(repo)

        supportFragmentManager.beginTransaction()
                .add(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

}
