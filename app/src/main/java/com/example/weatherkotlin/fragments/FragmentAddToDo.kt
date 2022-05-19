package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.databinding.FragmentAddToDoBinding
import com.example.weatherkotlin.util.configDateButton
import com.example.weatherkotlin.util.configTimeButton
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAddToDo.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAddToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentAddToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            priorityUrgentImportant.isChecked = true
            buttonSelectDeadlineDate.setOnClickListener {
                configDateButton(requireContext(), deadlineDate)
            }
            buttonSelectDeadlineHour.setOnClickListener {
                configTimeButton(requireContext(), deadlineHour)
            }
            saveAction.setOnClickListener {
                saveNewTask()
            }
        }
    }

    private fun saveNewTask() {
        if (isEntryValid()) {
            binding.let {
                viewModel.addNewToDoItem(
                    it.taskName.text.toString(),
                    it.taskDescription.text.toString(),
                    getPriority(),
                    it.deadlineDate.text.toString(),
                    it.deadlineHour.text.toString(),
                    it.completedSwitch.isChecked
                )
            }
            val action = FragmentAddToDoDirections.actionFragmentAddToDoToFragmentListToDo()
            findNavController().navigate(action)
            Snackbar.make(requireContext(), binding.root, "Lưu thành công", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            Snackbar.make(
                requireContext(),
                binding.root,
                "Dữ liệu nhập vào không hợp lệ!",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun isEntryValid(): Boolean {
        binding.let {
            return viewModel.isEntryValid(
                it.taskName.text.toString(),
                it.taskDescription.text.toString(),
                getPriority(),
                it.deadlineDate.text.toString(),
                it.deadlineHour.text.toString(),
            )
        }
    }

    private fun getPriority(): Int {
        return binding.let {
            when {
                it.priorityUrgentImportant.isChecked -> 1
                it.priorityNotUrgentImportant.isChecked -> 2
                it.priorityUrgentNotImportant.isChecked -> 3
                else -> 4
            }
        }
    }
}