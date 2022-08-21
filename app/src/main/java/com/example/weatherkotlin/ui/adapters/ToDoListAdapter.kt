package com.example.weatherkotlin.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.databinding.ItemToDoBinding
import com.example.weatherkotlin.data.domainModel.Task
import com.example.weatherkotlin.util.convertFromPattern1ToFullDate
import com.google.android.material.chip.Chip

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
                    if (it.length > 50) {
                        itemName.text = it.substring(0, 49).trim().plus("...")
                    } else {
                        itemName.text = it
                    }
                }
                itemDeadline.text =
                    itemToDo.deadlineDate.convertFromPattern1ToFullDate().plus(" - ")
                        .plus(itemToDo.deadlineHour)
                itemToDo.description.let {
                    if (it.length > 70) {
                        itemDescription.text = it.substring(0, 69).trim().plus("...")
                    } else {
                        itemDescription.text = it
                    }
                }
                chipGroup.removeAllViews() // Prevent duplicated chips
                when (itemToDo.isNotified) {
                    true -> {
                        chipGroup.addView(
                            createChip(
                                "Có thông báo",
                                this.root.context,
                                "Notification"
                            )
                        )
                    }
                    else -> {}
                }
                when (itemToDo.completed) {
                    true -> {
                        chipGroup.addView(
                            createChip(
                                "Đã hoàn thành",
                                this.root.context,
                                "Completion"
                            )
                        )
                    }
                    else -> {}
                }
                when (itemToDo.priority) {
                    1 -> {
                        chipGroup.addView(createChip("Khẩn cấp", this.root.context, "Priority"))
                        chipGroup.addView(createChip("Quan trọng", this.root.context, "Priority"))
                    }
                    2 -> {
                        chipGroup.addView(
                            createChip(
                                "Không khẩn cấp",
                                this.root.context,
                                "Priority"
                            )
                        )
                        chipGroup.addView(createChip("Quan trọng", this.root.context, "Priority"))
                    }
                    3 -> {
                        chipGroup.addView(createChip("Khẩn cấp", this.root.context, "Priority"))
                        chipGroup.addView(
                            createChip(
                                "Không quan trọng",
                                this.root.context,
                                "Priority"
                            )
                        )
                    }
                    else -> {
                        chipGroup.addView(
                            createChip(
                                "Không khẩn cấp",
                                this.root.context,
                                "Priority"
                            )
                        )
                        chipGroup.addView(
                            createChip(
                                "Không quan trọng",
                                this.root.context,
                                "Priority"
                            )
                        )
                    }
                }
                executePendingBindings()
            }
        }

        private fun createChip(chipName: String, ctx: Context, chipType: String): Chip {
            val chip = Chip(ctx)
            chip.apply {
                id = ViewCompat.generateViewId()
                text = chipName
                isCloseIconVisible = false
                shapeAppearanceModel = shapeAppearanceModel.withCornerSize(50.0f)
                when (chipType) {
                    "Notification" -> {
                        isChipIconVisible = true
                        setChipIconResource(R.drawable.ic_noti_on)
                    }
                    "Completion" -> {
                        isChipIconVisible = true
                        setChipIconResource(R.drawable.ic_check)
                    }
                    else -> {}
                }
            }
            return chip
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