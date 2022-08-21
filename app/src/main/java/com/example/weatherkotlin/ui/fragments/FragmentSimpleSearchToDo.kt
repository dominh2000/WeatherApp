package com.example.weatherkotlin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.databinding.FragmentSimpleSearchToDoBinding
import com.example.weatherkotlin.ui.adapters.ToDoListAdapter
import com.example.weatherkotlin.ui.viewModel.ToDoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FragmentSimpleSearchToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels()

    private var _binding: FragmentSimpleSearchToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSimpleSearchToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ToDoListAdapter {
            viewModel.onTaskClicked(it)
            val action =
                FragmentSimpleSearchToDoDirections.actionFragmentSimpleSearchToDoToFragmentAddToDo(
                    crudType = 1
                )
            findNavController().navigate(action)
        }

        viewModel.getAllToDosDsc()
        binding.recyclerViewSimpleSearch.adapter = adapter
        viewModel.toDoList.observe(viewLifecycleOwner) {
            adapter.modifyList(it)
        }
        binding.textEmptySearchResult.visibility = View.GONE

        binding.searchByName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Start a coroutine in Dispatchers.Main, bound to the Lifecycle of LifecycleOwner
                lifecycleScope.launch {
                    doFiltering(
                        newText,
                        binding.recyclerViewSimpleSearch.adapter as ToDoListAdapter
                    )
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Main-safe suspend fun to be called from Dispatchers.Main
     */
    suspend fun doFiltering(text: String, adapter: ToDoListAdapter) {
        // Switch from Dispatchers.Main to Dispatchers.Default to perform filtering
        // Only blocks the Default threads, NOT the Main thread
        withContext(Dispatchers.Default) {
            // Blocking call
            adapter.filter(text)
        }
    }
}