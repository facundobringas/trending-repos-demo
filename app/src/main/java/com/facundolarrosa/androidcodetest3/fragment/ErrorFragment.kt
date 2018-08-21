package com.facundolarrosa.androidcodetest3.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facundolarrosa.androidcodetest3.R
import kotlinx.android.synthetic.main.fragment_error.*

private const val ARG_ERROR_MESSAGE = "ERROR_MESSAGE"

class ErrorFragment : Fragment() {
    private var mParamErrorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParamErrorMessage = it.getString(ARG_ERROR_MESSAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_error.text = mParamErrorMessage
    }

    companion object {
        /**
         * @mParamErrorMessage mParamErrorMessage Parameter 1.
         * @return A new instance of fragment ErrorFragment.
         */
        @JvmStatic
        fun newInstance(paramErrorMessage: String?) =
                ErrorFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_ERROR_MESSAGE, paramErrorMessage)
                    }
                }
    }
}
