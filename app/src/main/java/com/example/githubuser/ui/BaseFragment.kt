package com.example.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding, VM: ViewModel>(
    private val inflateLayout : (LayoutInflater, ViewGroup?, Boolean) -> VB
): Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

//    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateLayout.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.initialize()
        observeData()
    }

    open fun VB.initialize(){}
    open fun observeData(){}

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}