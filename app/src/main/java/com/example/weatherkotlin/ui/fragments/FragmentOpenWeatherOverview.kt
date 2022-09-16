package com.example.weatherkotlin.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.dataSources.datastore.WeatherLocationDataStore
import com.example.weatherkotlin.databinding.FragmentOpenWeatherOverviewBinding
import com.example.weatherkotlin.ui.adapters.OpenWeatherDatesAdapter
import com.example.weatherkotlin.ui.viewModel.OpenWeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class FragmentOpenWeatherOverview : Fragment() {

    private val viewModel: OpenWeatherViewModel by activityViewModels()

    private var _binding: FragmentOpenWeatherOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherLocationDataStore: WeatherLocationDataStore
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                showToastAskingForPermission()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenWeatherOverviewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // addMenuProvider API to replace deprecated APIs
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.refresh_weather -> {
                        weatherLocationDataStore.locationFlow.asLiveData()
                            .observe(viewLifecycleOwner) {
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
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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

    private fun getCurrentLocation() {
        when (PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                getLocation()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    private fun getLocation() {
        try {
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
                            "Vị trí của bạn: Latt ${location.latitude.roundToInt()}, Long ${location.longitude.roundToInt()}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Không thể lấy vị trí. Hãy bật cài đặt vị trí trên thiết bị của bạn!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
            showToastAskingForPermission()
        }
    }

    private fun showToastAskingForPermission() {
        Toast.makeText(
            requireContext(),
            "Hãy cho phép quyền truy cập vị trí để sử dụng chức năng này!",
            Toast.LENGTH_SHORT
        ).show()
    }
}