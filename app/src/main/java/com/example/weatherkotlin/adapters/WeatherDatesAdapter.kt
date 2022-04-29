package com.example.weatherkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.databinding.ListItemBinding
import com.example.weatherkotlin.domain.WeatherOneDay
import com.example.weatherkotlin.util.showTemperature

class WeatherDatesAdapter(private val onItemClicked: (WeatherOneDay) -> Unit) :
    ListAdapter<WeatherOneDay, WeatherDatesAdapter.WeatherDatesViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<WeatherOneDay>() {
        override fun areItemsTheSame(oldItem: WeatherOneDay, newItem: WeatherOneDay): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherOneDay, newItem: WeatherOneDay): Boolean {
            return oldItem == newItem
        }
    }

    class WeatherDatesViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherOneDay: WeatherOneDay) {
            binding.dateItem.text = weatherOneDay.date
            binding.temperatureItem.text = showTemperature(weatherOneDay.minTemp, weatherOneDay.maxTemp)
            binding.stateAbbr = weatherOneDay.stateAbbr
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDatesViewHolder {
        val itemViewHolder = WeatherDatesViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        itemViewHolder.itemView.setOnClickListener {
            val position = itemViewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: WeatherDatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}