package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.weatherkotlin.ApplicationClass
import com.example.weatherkotlin.adapters.WeatherDatesAdapter
import com.example.weatherkotlin.databinding.FragmentListDatesBinding
import com.example.weatherkotlin.viewmodels.WeatherViewModel
import com.example.weatherkotlin.viewmodels.WeatherViewModelFactory

class FragmentListDates : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels {
        WeatherViewModelFactory(
            (activity?.application as ApplicationClass)
        )
    }

private var _binding: FragmentListDatesBinding? = null

private val binding get() = _binding!!

/**
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
arguments?.let {
}
}
 */

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    _binding = FragmentListDatesBinding.inflate(inflater, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.viewModel = viewModel
    return binding.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val adapter = WeatherDatesAdapter {
        viewModel.onWeatherItemClicked(it)
        val action = FragmentListDatesDirections.actionFragmentListDatesToFragmentDetail()
        view.findNavController().navigate(action)
    }
    binding.recyclerView.adapter = adapter
}

}