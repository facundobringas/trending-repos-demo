package com.facundolarrosa.androidcodetest3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_error.*

private const val ARG_PARAM1 = "param1"


class ErrorFragment : Fragment() {
    private var param1: String? = null
    private var listener: OnReloadListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_error.text = param1
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnReloadListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnReloadListener {
        fun onReloadListener()
    }

    companion object {
        /**
         * @param param1 Parameter 1.
         * @return A new instance of fragment ErrorFragment.
         */
        @JvmStatic
        fun newInstance(param1: String?) =
                ErrorFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
