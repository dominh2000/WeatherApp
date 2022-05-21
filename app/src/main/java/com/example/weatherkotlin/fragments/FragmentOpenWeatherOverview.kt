package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.adapters.OpenWeatherDatesAdapter
import com.example.weatherkotlin.databinding.FragmentOpenWeatherOverviewBinding
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModel
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentOpenWeatherOverview.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentOpenWeatherOverview : Fragment() {

    private val viewModel: OpenWeatherViewModel by activityViewModels {
        OpenWeatherViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentOpenWeatherOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentOpenWeatherOverviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = OpenWeatherDatesAdapter {
            viewModel.onWeatherItemClicked(it)
            val action =
                FragmentOpenWeatherOverviewDirections.actionFragmentOpenWeatherOverviewToFragmentOpenWeatherItemDetails()
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh_weather -> {
                viewModel.getOpenWeatherInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}