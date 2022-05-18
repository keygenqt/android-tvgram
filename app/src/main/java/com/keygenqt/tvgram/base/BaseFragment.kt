package com.keygenqt.tvgram.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<T> : Fragment() {

    private var _binding: T? = null
    private val binding get() = _binding!!

    protected val fragmentTransaction get() = requireActivity().supportFragmentManager

    abstract fun onCreateBinding(i: LayoutInflater, v: ViewGroup?): T
    abstract fun onCreateView(binding: T): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = onCreateBinding(inflater, container)
        return onCreateView(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}