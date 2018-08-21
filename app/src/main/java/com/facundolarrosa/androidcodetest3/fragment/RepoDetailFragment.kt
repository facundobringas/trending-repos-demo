package com.facundolarrosa.androidcodetest3.fragment


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.intentservice.HtttpIntentService
import com.facundolarrosa.androidcodetest3.model.Repo
import kotlinx.android.synthetic.main.fragment_repo_detail.*

private const val ARG_REPO = "repo"

/**
 * A simple [Fragment] subclass.
 * Use the [RepoDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RepoDetailFragment : Fragment() {
    private var mRepo: Repo? = null
    private lateinit var mBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mRepo = it.getParcelable(ARG_REPO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {

                when (intent?.action) {
                    HtttpIntentService.GET_README_SUCCESS -> onGetReadMeSuccess(intent?.getStringExtra(HtttpIntentService.GET_README_RESULT)!!)
                }
            }
        }

        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = mRepo?.name
        tv_detail.text = mRepo?.description

        getReadMe()
    }

    override fun onStart() {
        super.onStart()

        val intentFilter =  IntentFilter()
        intentFilter.addAction(HtttpIntentService.GET_README_SUCCESS)
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(mBroadcastReceiver,intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mBroadcastReceiver)
    }


    fun getReadMe(){
        HtttpIntentService.startGetReadMe(activity!!, mRepo?.owner?.login!!, mRepo?.name!!)
    }

    fun onGetReadMeSuccess(readme: String){
        md_view.loadMarkdown(readme)
    }

    companion object {
        /**
         * @param repo Parameter 1.
         * @return A new instance of fragment RepoDetailFragment.
         */
        @JvmStatic
        fun newInstance(repo: Repo) =
                RepoDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_REPO, repo)
                    }
                }
    }
}
