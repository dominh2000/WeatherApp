package com.example.weatherkotlin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.R
import com.example.weatherkotlin.const.DATE_FORMAT_PATTERN_1
import com.example.weatherkotlin.databinding.FragmentAdvancedSearchToDoBinding
import com.example.weatherkotlin.ui.viewModel.AdvancedSearchStatus
import com.example.weatherkotlin.ui.viewModel.ToDoViewModel
import com.example.weatherkotlin.util.calculateMillisecondsForAdvancedSearch
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FragmentAdvancedSearchToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels()

    private var _binding: FragmentAdvancedSearchToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdvancedSearchToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            prioritySpinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                resources.getStringArray(R.array.priority_level)
            )
            notifiedSpinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                resources.getStringArray(R.array.notified_state)
            )
            completedSpinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                resources.getStringArray(R.array.completed_state)
            )

            buttonSelectStartDate.setOnClickListener {
                configDateButton(buttonSelectStartDate.text.toString(), startDate)
            }
            buttonSelectEndDate.setOnClickListener {
                configDateButton(buttonSelectEndDate.text.toString(), endDate)
            }

            viewModel.advancedSearchStatus.observe(viewLifecycleOwner) {
                if (it == AdvancedSearchStatus.LOADING) {
                    binding.statusIndicator.visibility = View.VISIBLE
                } else {
                    binding.statusIndicator.visibility = View.GONE
                    if (viewModel.startAdvancedSearch) {
                        if (viewModel.advancedSearchResultList.value!!.isEmpty()) {
                            val alertDialogBuilder =
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("Thông báo")
                                    .setMessage("Không có kết quả tìm kiếm")
                                    .setPositiveButton("OK") { _, _ -> }
                                    .create()
                            alertDialogBuilder.show()
                        } else {
                            val action =
                                FragmentAdvancedSearchToDoDirections.actionFragmentAdvancedSearchToDoToFragmentAdvancedSearchToDoResult()
                            findNavController().navigate(action)
                        }
                        viewModel.startAdvancedSearch = false
                    }
                }
            }

            advancedSearchAction.setOnClickListener {
                if (startDate.text.isNotEmpty() && endDate.text.isNotEmpty()) {
                    if (calculateMillisecondsForAdvancedSearch(startDate.text.toString())
                        <= calculateMillisecondsForAdvancedSearch(endDate.text.toString())
                    ) {
                        viewModel.startAdvancedSearch = true
                        viewModel.filterByAdvancedSearch(
                            startDate.text.toString(),
                            endDate.text.toString(),
                            (prioritySpinner.selectedItemId + 1).toInt(),
                            (completedSpinner.selectedItemId).toInt(),
                            (notifiedSpinner.selectedItemId).toInt()
                        )
                    } else {
                        Snackbar.make(
                            requireContext(),
                            root,
                            "Ngày bắt đầu không được lớn hơn ngày kết thúc!",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAnchorView(R.id.bottom_nav)
                            .show()
                    }
                } else {
                    Snackbar.make(
                        requireContext(),
                        root,
                        "Hãy chọn đủ ngày.",
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(R.id.bottom_nav)
                        .show()
                }
            }

            cancelAction.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configDateButton(title: String, textView: TextView) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            val selectedDate =
                SimpleDateFormat(DATE_FORMAT_PATTERN_1, Locale.getDefault()).format(calendar.time)
            textView.text = selectedDate
        }

        datePicker.show(childFragmentManager, "date_picker")
    }
}