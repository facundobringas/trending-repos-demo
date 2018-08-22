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
import com.facundolarrosa.androidcodetest3.glide.GlideApp
import com.facundolarrosa.androidcodetest3.intentservice.HttpIntentService
import com.facundolarrosa.androidcodetest3.model.Repo
import kotlinx.android.synthetic.main.fragment_repo_detail.*
//import ru.noties.markwon.Markwon

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
    private var mListener: OnFragmentFinishedLoadingListener? = null

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
                    HttpIntentService.GET_README_SUCCESS -> onGetReadMeSuccess(intent.getStringExtra(HttpIntentService.GET_README_RESULT)!!)
                    HttpIntentService.GET_README_ERROR -> onGetReadMeError()
                }
            }
        }

        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_name.text = mRepo?.name
        tv_detail.text = mRepo?.description
        tv_owner.text = mRepo?.owner?.login
        tv_language.text = mRepo?.language
        tv_stars.text = mRepo?.stargazersCount.toString()
        tv_forks.text = mRepo?.forks.toString()

        GlideApp.with(this)
                .load(mRepo?.owner?.avatarUrl)
                .placeholder(R.drawable.img_missing_avatar)
                .into(iv_owner_avatar);

        getReadMe()
    }

    override fun onStart() {
        super.onStart()

        val intentFilter =  IntentFilter()
        intentFilter.addAction(HttpIntentService.GET_README_SUCCESS)
        intentFilter.addAction(HttpIntentService.GET_README_ERROR)
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(mBroadcastReceiver,intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mBroadcastReceiver)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentFinishedLoadingListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentFinishedLoadingListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun getReadMe(){
        HttpIntentService.startGetReadMe(activity!!, mRepo?.owner?.login!!, mRepo?.name!!)
    }

    private fun onGetReadMeSuccess(readme: String){
        tv_readme_error.visibility = View.GONE
        md_view.setMarkDownText(readme)
        mListener?.onFragmentFinishedLoading()
    }

    private fun onGetReadMeError() {
        tv_readme_error.visibility = View.VISIBLE
        mListener?.onFragmentFinishedLoading()
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

    interface OnFragmentFinishedLoadingListener {
        fun onFragmentFinishedLoading()
    }
}
