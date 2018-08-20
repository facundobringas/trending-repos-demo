package com.facundolarrosa.androidcodetest3.repo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facundolarrosa.androidcodetest3.R
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
    private var repo: Repo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repo = it.getParcelable(ARG_REPO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = repo?.name
        tv_detail.text = repo?.description
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
