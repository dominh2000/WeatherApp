package com.example.weatherkotlin.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.adapters.OpenWeatherDatesAdapter
import com.example.weatherkotlin.databinding.FragmentOpenWeatherOverviewBinding
import com.example.weatherkotlin.datastore.WeatherLocationDataStore
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModel
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class FragmentOpenWeatherOverview : Fragment() {

    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private val viewModel: OpenWeatherViewModel by activityViewModels {
        OpenWeatherViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentOpenWeatherOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherLocationDataStore: WeatherLocationDataStore
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

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
        weatherLocationDataStore = WeatherLocationDataStore(requireContext())
        weatherLocationDataStore.locationFlow.asLiveData().observe(viewLifecycleOwner) {
            if (it[0] == 0.0 && it[1] == 0.0) {
                viewModel.getOpenWeatherInfo()
            } else {
                viewModel.getOpenWeatherInfoByCoord(it[0], it[1])
            }
        }
        val adapter = OpenWeatherDatesAdapter {
            viewModel.onWeatherItemClicked(it)
            val action =
                FragmentOpenWeatherOverviewDirections.actionFragmentOpenWeatherOverviewToFragmentOpenWeatherItemDetails()
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh_weather -> {
                weatherLocationDataStore.locationFlow.asLiveData().observe(viewLifecycleOwner) {
                    if (it[0] == 0.0 && it[1] == 0.0) {
                        viewModel.getOpenWeatherInfo()
                    } else {
                        viewModel.getOpenWeatherInfoByCoord(it[0], it[1])
                    }
                }
                true
            }
            R.id.locate_weather -> {
                getCurrentLocation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.getOpenWeatherInfoByCoord(location.latitude, location.longitude)
                    lifecycleScope.launch {
                        weatherLocationDataStore.saveWeatherLocationToPreferencesStore(
                            location.latitude,
                            location.longitude,
                            requireContext()
                        )
                    }
                    Toast.makeText(
                        requireContext(),
                        "V??? tr?? c???a b???n: Latt ${location.latitude.roundToInt()}, Long ${location.longitude.roundToInt()}.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Kh??ng th??? l???y v??? tr??. H??y b???t c??i ?????t v??? tr?? tr??n thi???t b??? c???a b???n!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "H??y cho ph??p quy???n truy c???p v??? tr?? ????? s??? d???ng ch???c n??ng n??y!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}