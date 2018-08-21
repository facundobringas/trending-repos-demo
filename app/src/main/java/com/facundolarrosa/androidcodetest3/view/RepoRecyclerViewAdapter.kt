package com.facundolarrosa.androidcodetest3.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facundolarrosa.androidcodetest3.R
import com.facundolarrosa.androidcodetest3.model.Repo
import kotlinx.android.synthetic.main.repo_item.view.*


class RepoRecyclerViewAdapter(
        var mRepos: List<Repo>,
        private val mOnClickListener: View.OnClickListener)
    : RecyclerView.Adapter<RepoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = mRepos[position]
        holder.mName.text = repo.name
        holder.mDescription.text = repo.description

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
