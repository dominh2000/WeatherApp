package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.databinding.FragmentOpenWeatherItemDetailsBinding
import com.example.weatherkotlin.util.convertDateFromPattern5ToPattern4
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModel
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModelFactory

class FragmentOpenWeatherItemDetails : Fragment() {

    private val viewModel: OpenWeatherViewModel by activityViewModels {
        OpenWeatherViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentOpenWeatherItemDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenWeatherItemDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.date.text =
            viewModel.weatherItem.value!!.dateTimeText.convertDateFromPattern5ToPattern4()
        viewModel.let {
            binding.precipitation.text =
                it.getFormattedPrecipitation(it.weatherItem.value!!.precipitation.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}