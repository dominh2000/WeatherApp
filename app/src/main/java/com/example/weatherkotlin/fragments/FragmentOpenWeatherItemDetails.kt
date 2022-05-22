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
        val preUnformatted = viewModel.weatherItem.value!!.precipitation.toString()
        if (preUnformatted.length == 3) {
            if (preUnformatted == "1.0") {
                binding.precipitation.text = "100%"
            } else if (preUnformatted[2] != '0') {
                binding.precipitation.text = preUnformatted[2].toString().plus("0%")
            } else {
                binding.precipitation.text = preUnformatted[2].toString().plus("%")
            }
        } else {
            val precipitation =
                viewModel.weatherItem.value!!.precipitation.toString().substring(2, 4)
            if (precipitation[0] == '0') {
                binding.precipitation.text = precipitation[1].toString().plus("%")
            } else {
                binding.precipitation.text = precipitation.plus("%")
            }
        }

    }
}