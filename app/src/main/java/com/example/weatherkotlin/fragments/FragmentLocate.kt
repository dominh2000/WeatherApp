package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherkotlin.databinding.FragmentLocateBinding

class FragmentLocate: Fragment() {

    private var _binding: FragmentLocateBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocateBinding.inflate(inflater, container, false)
        return binding.root
    }
}