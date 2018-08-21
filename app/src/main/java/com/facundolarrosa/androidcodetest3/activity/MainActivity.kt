package com.facundolarrosa.androidcodetest3.activity

import android.app.FragmentManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.facundolarrosa.androidcodetest3.fragment.ErrorFragment
import com.facundolarrosa.androidcodetest3.fragment.NoResultsFragment
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.fragment.RepoDetailFragment
import com.facundolarrosa.androidcodetest3.model.Repo
import com.facundolarrosa.androidcodetest3.intentservice.HtttpIntentService
import com.facundolarrosa.androidcodetest3.view.RepoRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.repo_list.*

private const val ERROR_STATE = "ERROR_STATE"

class MainActivity : AppCompatActivity(){

    private lateinit var mRepoAdapter: RepoRecyclerViewAdapter
    private lateinit var mBroadcastReceiver: BroadcastReceiver
    private var mCachedRepos: List<Repo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = getString(R.string.main_activity_title)

        val onClickListener: View.OnClickListener = View.OnClickListener { v ->
            val repo = v.tag as Repo
            onRepoItemClick(repo)
        }

        mRepoAdapter = RepoRecyclerViewAdapter(ArrayList(), onClickListener)

        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {

                when (intent?.action) {
                    HtttpIntentService.SEARCH_REPOS_SUCCESS -> onSearchReposSuccess(intent?.getParcelableArrayListExtra(HtttpIntentService.SEARCH_REPOS_RESULT)!! )
                    HtttpIntentService.SEARCH_REPOS_ERROR -> onSearchReposError(intent?.getStringExtra(HtttpIntentService.SEARCH_REPOS_ERROR_MESSAGE))
                }
            }
        }

        if (rv_repo_list is RecyclerView) {
            with(rv_repo_list) {
                adapter = mRepoAdapter
            }
        }

        searchRepos()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_refresh -> searchRepos()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val intentFilter =  IntentFilter()
        intentFilter.addAction(HtttpIntentService.SEARCH_REPOS_SUCCESS)
        intentFilter.addAction(HtttpIntentService.SEARCH_REPOS_ERROR)
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            if(supportFragmentManager.getBackStackEntryAt(0).name == ERROR_STATE) {
                if( mCachedRepos == null ){
                    finish()
                }
            }else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                title = getString(R.string.main_activity_title)
                invalidateOptionsMenu()
            }
        }
        super.onBackPressed()
    }

    private fun searchRepos(){
        HtttpIntentService.startSearchRepos(this);
        main_progress_bar.visibility = View.VISIBLE
    }

    private fun onSearchReposSuccess(repos : ArrayList<Repo>){
        // Out from error state if present
        supportFragmentManager.popBackStack(ERROR_STATE, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        if(repos.size > 0) {
            mRepoAdapter.mRepos = repos
            mRepoAdapter.notifyDataSetChanged()
        }else{
            val fragment = NoResultsFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(ERROR_STATE)
                    .add(R.id.main_container, fragment)
                    .commit()
        }
        mCachedRepos = repos
        main_progress_bar.visibility = View.GONE
    }

    fun onSearchReposError(error: String?) {
        val fragment = ErrorFragment.newInstance(error)
        supportFragmentManager
                .beginTransaction()
                .addToBackStack(ERROR_STATE)
                .replace(R.id.main_container, fragment)
                .commit()

        main_progress_bar.visibility = View.GONE
    }

    fun onRepoItemClick(repo: Repo) {
        val fragment = RepoDetailFragment.newInstance(repo)

        supportFragmentManager.beginTransaction()
                .add(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.menu.removeItem(R.id.action_refresh)
    }

}
