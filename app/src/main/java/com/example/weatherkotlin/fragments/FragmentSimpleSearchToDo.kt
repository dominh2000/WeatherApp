package com.example.weatherkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.adapters.ToDoListAdapter
import com.example.weatherkotlin.databinding.FragmentSimpleSearchToDoBinding
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSimpleSearchToDo : Fragment() {

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

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
                doFiltering(newText, binding.recyclerViewSimpleSearch.adapter as ToDoListAdapter)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Run the filter blocking call inside a coroutine which is bound to Lifecycle of LifecycleOwner
     */
    fun doFiltering(text: String, adapter: ToDoListAdapter) {
        lifecycleScope.launch {
            // Switch the Dispatcher to Default to perform filtering
            // Only blocks the Default thread, NOT the Main thread
            withContext(Dispatchers.Default) {
                // Blocking call
                adapter.filter(text)
            }
        }
    }
}