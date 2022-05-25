package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.adapters.ToDoListAdapter
import com.example.weatherkotlin.databinding.FragmentListToDoBinding
import com.example.weatherkotlin.util.launchLogoutAlertDialog
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FragmentListToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentListToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentListToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val snackBarType = FragmentListToDoArgs.fromBundle(requireArguments()).snackBarType
        val userDisplayName = FragmentListToDoArgs.fromBundle(requireArguments()).userDisplayName

        when (snackBarType) {
            1 -> {
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    "Lưu thành công.",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
                requireArguments().clear()
            }
            2 -> {
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    "Xóa thành công.",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
                requireArguments().clear()
            }
            3 -> {
                val msg = "Người dùng $userDisplayName đăng nhập thành công!"
                Snackbar.make(requireContext(), binding.root, msg, Snackbar.LENGTH_SHORT)
                    .show()
                requireArguments().clear()
            }
            else -> {
            }
        }

        binding.buttonAdd.setOnClickListener {
            val action =
                FragmentListToDoDirections.actionFragmentListToDoToFragmentAddToDo(crudType = 0)
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            launchLogoutAlertDialog(requireContext(), findNavController())
        }

        val adapter = ToDoListAdapter {
            viewModel.onTaskClicked(it)
            val action =
                FragmentListToDoDirections.actionFragmentListToDoToFragmentAddToDo(crudType = 1)
            findNavController().navigate(action)
        }

        viewModel.getAllToDosDsc()
        binding.recyclerViewToDoList.adapter = adapter
        binding.recyclerViewToDoList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        viewModel.toDoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.to_do_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                launchLogoutAlertDialog(requireContext(), findNavController())
                true
            }
            R.id.closest_deadline -> {
                viewModel.filterByClosestDeadline()
                true
            }
            R.id.furthest_deadline -> {
                viewModel.filterByFurthestDeadline()
                true
            }
            R.id.by_priority_1 -> {
                viewModel.filterByPriority1()
                true
            }
            R.id.by_priority_2 -> {
                viewModel.filterByPriority2()
                true
            }
            R.id.by_priority_3 -> {
                viewModel.filterByPriority3()
                true
            }
            R.id.by_priority_4 -> {
                viewModel.filterByPriority4()
                true
            }
            R.id.not_completed -> {
                viewModel.filterByNotCompleted()
                true
            }
            R.id.completed -> {
                viewModel.filterByCompleted()
                true
            }
            R.id.not_set_notified -> {
                viewModel.filterByNotNotified()
                true
            }
            R.id.set_notified -> {
                viewModel.filterByNotified()
                true
            }
            R.id.simple_search -> {
                val action =
                    FragmentListToDoDirections.actionFragmentListToDoToFragmentSimpleSearchToDo()
                findNavController().navigate(action)
                true
            }
            R.id.advanced_search -> {
                val action =
                    FragmentListToDoDirections.actionFragmentListToDoToFragmentAdvancedSearchToDo()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}