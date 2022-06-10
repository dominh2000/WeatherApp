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
        viewModel.let { weatherVM ->
            binding.apply {
                weatherVM.weatherItem.observe(viewLifecycleOwner) {
                    date.text = it.dateTimeText.convertDateFromPattern5ToPattern4()
                    precipitation.text =
                        weatherVM.getFormattedPrecipitation(it.precipitation.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}