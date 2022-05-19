package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.databinding.FragmentUpdateDeleteToDoBinding
import com.example.weatherkotlin.util.configDateButton
import com.example.weatherkotlin.util.configTimeButton
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class FragmentUpdateDeleteToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentUpdateDeleteToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateDeleteToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel.selectedTask.value?.let {
                taskName.setText(it.name)
                taskDescription.setText(it.description)
                when (it.priority) {
                    1 -> priorityUrgentImportant.isChecked = true
                    2 -> priorityNotUrgentImportant.isChecked = true
                    3 -> priorityUrgentNotImportant.isChecked = true
                    else -> priorityNotUrgentNotImportant.isChecked = true
                }
                deadlineDate.text = it.deadlineDate
                deadlineHour.text = it.deadlineHour
                completedSwitch.isChecked = it.completed
            }

            buttonSelectDeadlineDate.setOnClickListener {
                configDateButton(requireContext(), deadlineDate)
            }
            buttonSelectDeadlineHour.setOnClickListener {
                configTimeButton(requireContext(), deadlineHour)
            }

            updateAction.setOnClickListener {
                updateTask()
            }
            deleteAction.setOnClickListener {
                deleteTask()
            }
        }
    }

    private fun updateTask() {
        if (isEntryValid()) {
            binding.let {
                viewModel.updateToDoItem(
                    viewModel.selectedTask.value!!.id,
                    it.taskName.text.toString(),
                    it.taskDescription.text.toString(),
                    getPriority(),
                    it.deadlineDate.text.toString(),
                    it.deadlineHour.text.toString(),
                    it.completedSwitch.isChecked
                )
            }
            val action =
                FragmentUpdateDeleteToDoDirections.actionFragmentUpdateDeleteToDoToFragmentListToDo()
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

    private fun deleteTask() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Thông báo")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Bạn có chắc chắn muốn xóa nhắc việc này?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteToDoItem(viewModel.selectedTask.value!!)
                val action =
                    FragmentUpdateDeleteToDoDirections.actionFragmentUpdateDeleteToDoToFragmentListToDo()
                findNavController().navigate(action)
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    "Xóa thành công",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Không") { _, _ -> }
            .create()
        alertDialogBuilder.show()
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