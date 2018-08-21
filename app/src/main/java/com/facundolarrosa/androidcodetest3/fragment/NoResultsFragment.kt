package com.facundolarrosa.androidcodetest3.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facundolarrosa.androidcodetest3.R

class NoResultsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_results, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance() = NoResultsFragment()
    }
}
