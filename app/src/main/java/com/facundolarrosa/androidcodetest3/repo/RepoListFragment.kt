package com.facundolarrosa.androidcodetest3.repo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.model.ApiResult
import com.facundolarrosa.androidcodetest3.model.Repo
import com.facundolarrosa.androidcodetest3.rest.AppRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RepoListFragment.OnRepoListListener] interface
 * to handle interaction events.
 * Use the [RepoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RepoListFragment : Fragment() {

    private var listener: OnRepoListListener? = null
    private lateinit var repoAdapter: RepoRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.repo_list, container, false)

        repoAdapter = RepoRecyclerViewAdapter(ArrayList(), listener)

        if (view is RecyclerView) {
            with(view) {
                adapter = repoAdapter
            }
        }

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRepoList()
    }

    fun loadRepoList() {
        val call = AppRetrofit(activity!!).searchService.search()
        call.enqueue(object: Callback<ApiResult<Repo>?> {
            override fun onResponse(call: Call<ApiResult<Repo>?>?,
                                    response: Response<ApiResult<Repo>?>?) {

                response?.body()?.let {
                    val repos: List<Repo> = it.items
                    loadRepos(repos)
                }

            }

            override fun onFailure(call: Call<ApiResult<Repo>?>?,
                                   t: Throwable?) {

            }
        })
    }

    private fun loadRepos(repos : List<Repo>){
        repoAdapter.mRepos = repos
        repoAdapter.notifyDataSetChanged()
        listener?.onRepoListLoad()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRepoListListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnRepoClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnRepoListListener {
        fun onRepoListLoad()
        fun onRepoListError(uri: Uri)
        fun onRepoListItemClick(repo: Repo)
    }

    companion object {
        /**
         * @return A new instance of fragment RepoListFragment.
         */
        @JvmStatic
        fun newInstance() = RepoListFragment()
    }
}
