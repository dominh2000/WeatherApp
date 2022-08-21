package com.example.weatherkotlin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherkotlin.databinding.FragmentSearchLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearchLocation : Fragment() {

    private var _binding: FragmentSearchLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchLocationBinding.inflate(inflater, container, false)
        return binding.root
    }
}