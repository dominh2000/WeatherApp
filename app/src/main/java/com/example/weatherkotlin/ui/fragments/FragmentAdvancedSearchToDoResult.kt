package com.example.weatherkotlin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.ui.adapters.ToDoListAdapter
import com.example.weatherkotlin.databinding.FragmentAdvancedSearchToDoResultBinding
import com.example.weatherkotlin.ui.viewModel.ToDoViewModel
import com.example.weatherkotlin.ui.viewModel.ToDoViewModelFactory

class FragmentAdvancedSearchToDoResult : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentAdvancedSearchToDoResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdvancedSearchToDoResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val adapter = ToDoListAdapter {
                viewModel.onTaskClicked(it)
                val action =
                    FragmentAdvancedSearchToDoResultDirections.actionFragmentAdvancedSearchToDoResultToFragmentAddToDo(
                        crudType = 1
                    )
                findNavController().navigate(action)
            }
            recyclerViewAdvancedSearchResult.adapter = adapter
            viewModel.advancedSearchResultList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            buttonAdvancedSearch.setOnClickListener {
                findNavController().popBackStack()
            }
            buttonBackToDoList.setOnClickListener {
                val action =
                    FragmentAdvancedSearchToDoResultDirections.actionFragmentAdvancedSearchToDoResultToFragmentListToDo()
                findNavController().navigate(action)
            }
        }
    }
}