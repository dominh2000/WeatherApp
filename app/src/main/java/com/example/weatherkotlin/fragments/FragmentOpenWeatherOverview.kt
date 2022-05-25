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
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.adapters.OpenWeatherDatesAdapter
import com.example.weatherkotlin.databinding.FragmentOpenWeatherOverviewBinding
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModel
import com.example.weatherkotlin.viewmodels.OpenWeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentOpenWeatherOverview.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentOpenWeatherOverview : Fragment() {

    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private val viewModel: OpenWeatherViewModel by activityViewModels {
        OpenWeatherViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentOpenWeatherOverviewBinding? = null
    private val binding get() = _binding!!
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
                viewModel.getOpenWeatherInfo()
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
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Hãy cho phép quyền truy cập vị trí để sử dụng chức năng này!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}