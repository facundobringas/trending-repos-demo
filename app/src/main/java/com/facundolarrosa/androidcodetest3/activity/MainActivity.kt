package com.facundolarrosa.androidcodetest3.activity

import android.app.Fragment
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import android.view.View
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.repo.RepoDetailFragment
import com.facundolarrosa.androidcodetest3.model.Repo
import com.facundolarrosa.androidcodetest3.repo.RepoListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RepoListFragment.OnRepoListListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.main_activity_title)

        val fragment = RepoListFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
    }

    override fun onRepoListLoad() {
        main_progress_bar.visibility = View.GONE
    }

    override fun onRepoListError(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRepoListItemClick(repo: Repo) {
        val fragment = RepoDetailFragment.newInstance(repo)

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0 ){
            if(supportFragmentManager.backStackEntryCount == 1){
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                title = getString(R.string.main_activity_title)
            }
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item)
    }
}
