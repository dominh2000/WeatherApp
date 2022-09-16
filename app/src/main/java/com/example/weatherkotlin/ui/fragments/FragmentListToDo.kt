package com.example.weatherkotlin.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.databinding.FragmentListToDoBinding
import com.example.weatherkotlin.ui.adapters.ToDoListAdapter
import com.example.weatherkotlin.ui.viewModel.ToDoViewModel
import com.example.weatherkotlin.util.launchLogoutAlertDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentListToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels()

    private var _binding: FragmentListToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // addMenuProvider API to replace deprecated APIs
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.to_do_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
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
                        viewModel.filterByPriority(1)
                        true
                    }
                    R.id.by_priority_2 -> {
                        viewModel.filterByPriority(2)
                        true
                    }
                    R.id.by_priority_3 -> {
                        viewModel.filterByPriority(3)
                        true
                    }
                    R.id.by_priority_4 -> {
                        viewModel.filterByPriority(4)
                        true
                    }
                    R.id.not_completed -> {
                        viewModel.filterByCompleteState(0)
                        true
                    }
                    R.id.completed -> {
                        viewModel.filterByCompleteState(1)
                        true
                    }
                    R.id.not_set_notified -> {
                        viewModel.filterByNotificationState(0)
                        true
                    }
                    R.id.set_notified -> {
                        viewModel.filterByNotificationState(1)
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
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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

}