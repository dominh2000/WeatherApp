package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.adapters.ToDoListAdapter
import com.example.weatherkotlin.const.DATE_FORMAT_PATTERN_1
import com.example.weatherkotlin.databinding.FragmentAdvancedSearchToDoBinding
import com.example.weatherkotlin.util.calculateMillisecondsForAdvancedSearch
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class FragmentAdvancedSearchToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

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
            val adapter = ToDoListAdapter {
                viewModel.onTaskClicked(it)
                val action =
                    FragmentAdvancedSearchToDoDirections.actionFragmentAdvancedSearchToDoToFragmentAddToDo(
                        crudType = 1
                    )
                findNavController().navigate(action)
            }

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

            recyclerViewAdvancedSearch.adapter = adapter
            viewModel.toDoList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            recyclerViewAdvancedSearch.visibility = View.GONE
            textEmptySearchResult.visibility = View.GONE

            buttonSelectStartDate.setOnClickListener {
                configDateButton(buttonSelectStartDate.text.toString(), startDate)
            }
            buttonSelectEndDate.setOnClickListener {
                configDateButton(buttonSelectEndDate.text.toString(), endDate)
            }
            advancedSearchAction.setOnClickListener {
                if (startDate.text.isNotEmpty() && endDate.text.isNotEmpty()) {
                    if (calculateMillisecondsForAdvancedSearch(startDate.text.toString())
                        <= calculateMillisecondsForAdvancedSearch(endDate.text.toString())
                    ) {
                        viewModel.filterByAdvancedSearch(
                            startDate.text.toString(),
                            endDate.text.toString(),
                            (prioritySpinner.selectedItemId + 1).toInt(),
                            (completedSpinner.selectedItemId).toInt(),
                            (notifiedSpinner.selectedItemId).toInt()
                        )
                        viewModel.toDoList.observe(viewLifecycleOwner) {
                            if (it.isEmpty()) {
                                textEmptySearchResult.visibility = View.VISIBLE
                                recyclerViewAdvancedSearch.visibility = View.GONE
                            } else {
                                textEmptySearchResult.visibility = View.GONE
                                recyclerViewAdvancedSearch.visibility = View.VISIBLE
                            }
                        }
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