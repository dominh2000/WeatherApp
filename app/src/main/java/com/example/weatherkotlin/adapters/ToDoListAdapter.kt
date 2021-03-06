package com.example.weatherkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.databinding.ItemToDoBinding
import com.example.weatherkotlin.domain.Task
import com.example.weatherkotlin.util.convertFromPattern1ToFullDate

class ToDoListAdapter(private val onItemClicked: (Task) -> Unit) :
    ListAdapter<Task, ToDoListAdapter.ToDoViewHolder>(DiffCallbackToDoItem) {

    private var unfilteredList = listOf<Task>()

    companion object DiffCallbackToDoItem : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    class ToDoViewHolder(private var binding: ItemToDoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemToDo: Task) {
            binding.apply {
                itemToDo.name.let {
                    if (it.length > 20) {
                        itemName.text = it.substring(0, 19).trim().plus("...")
                    } else {
                        itemName.text = it
                    }
                }
                itemDeadline.text =
                    itemToDo.deadlineDate.convertFromPattern1ToFullDate().plus(" - ")
                        .plus(itemToDo.deadlineHour)
                itemPriority.text = when (itemToDo.priority) {
                    1 -> "KC - QT"
                    2 -> "KKC - QT"
                    3 -> "KC - KQT"
                    else -> "KKC - KQT"
                }
                itemToDo.description.let {
                    if (it.length > 50) {
                        itemDescription.text = it.substring(0, 49).trim().plus("...")
                    } else {
                        itemDescription.text = it
                    }
                }
                when (itemToDo.isNotified) {
                    true -> {
                        imageAlarm.visibility = View.VISIBLE
                        imageAlarm.setImageResource(R.drawable.ic_noti_on)
                    }
                    else -> imageAlarm.visibility = View.GONE
                }
                when (itemToDo.completed) {
                    true -> {
                        imageStateComplete.visibility = View.VISIBLE
                        imageStateComplete.setImageResource(R.drawable.ic_check)
                    }
                    else -> imageStateComplete.visibility = View.GONE
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemViewHolder = ToDoViewHolder(
            ItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        itemViewHolder.itemView.setOnClickListener {
            val position = itemViewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun modifyList(list: List<Task>) {
        unfilteredList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<Task>()

        if (!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.name.lowercase().contains(query.toString().lowercase())
            })
        } else {
            list.addAll(unfilteredList)
        }

        submitList(list)
    }
}