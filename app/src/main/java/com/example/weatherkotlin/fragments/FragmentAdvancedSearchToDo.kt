package com.example.weatherkotlin.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
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
import com.example.weatherkotlin.databinding.FragmentAdvancedSearchToDoBinding
import com.example.weatherkotlin.util.DATE_FORMAT_PATTERN_1
import com.example.weatherkotlin.util.calculateMillisecondsForAdvancedSearch
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
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
                configDateButton(requireContext(), startDate)
            }
            buttonSelectEndDate.setOnClickListener {
                configDateButton(requireContext(), endDate)
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
                        ).show()
                    }
                } else {
                    Snackbar.make(requireContext(), root, "Hãy chọn đủ ngày.", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun configDateButton(ctx: Context, textView: TextView) {
        val datePickerDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog(ctx)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        datePickerDialog.setOnDateSetListener { _, i, i2, i3 ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, i)
            calendar.set(Calendar.MONTH, i2)
            calendar.set(Calendar.DAY_OF_MONTH, i3)
            val selectedDate =
                SimpleDateFormat(DATE_FORMAT_PATTERN_1, Locale.getDefault()).format(calendar.time)
            textView.text = selectedDate
        }
        datePickerDialog.show()
    }
}