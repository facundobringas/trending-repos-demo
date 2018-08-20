package com.facundolarrosa.androidcodetest3.repo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.model.Repo
import kotlinx.android.synthetic.main.repo_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class RepoRecyclerViewAdapter(
        var mRepos: List<Repo>,
        private val mListener: RepoListFragment.OnRepoListListener?)
    : RecyclerView.Adapter<RepoRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val repo = v.tag as Repo
            mListener?.onRepoListItemClick(repo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = mRepos[position]
        holder.mName.text = repo.name
        repo.description?.let {
            holder.mDescription.text = it
        }

        with(holder.mView) {
            tag = repo
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mRepos.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mName: TextView = mView.name
        val mDescription: TextView = mView.description

        override fun toString(): String {
            return super.toString() + " '" + mName.text + "'"
        }
    }
}
