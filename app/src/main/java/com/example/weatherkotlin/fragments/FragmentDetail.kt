package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.databinding.FragmentDetailBinding
import com.example.weatherkotlin.viewmodels.WeatherViewModel
import com.example.weatherkotlin.viewmodels.WeatherViewModelFactory

class FragmentDetail : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels {
        WeatherViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}