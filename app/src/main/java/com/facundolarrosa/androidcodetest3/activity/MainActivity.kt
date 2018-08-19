package com.facundolarrosa.androidcodetest3.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.rest.AppRetrofit
import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.model.Repo
import com.facundolarrosa.androidcodetest3.repo.RepoListFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), RepoListFragment.OnRepoListListener {

    private var repos: List<Repo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = RepoListFragment()

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    override fun onRepoListLoaded(repos: List<Repo>) {
        this.repos = repos;
        main_progress_bar.visibility = View.GONE
    }

    override fun onRepoListError(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRepoClick(repo: Repo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
