package com.example.weatherkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.databinding.ListItemBinding
import com.example.weatherkotlin.network.TotalWeather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherDatesAdapter(private val onItemClicked: (TotalWeather) -> Unit) :
    ListAdapter<TotalWeather, WeatherDatesAdapter.WeatherDatesViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<TotalWeather>() {
        override fun areItemsTheSame(oldItem: TotalWeather, newItem: TotalWeather): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TotalWeather, newItem: TotalWeather): Boolean {
            return oldItem == newItem
        }
    }

    class WeatherDatesViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(totalWeather: TotalWeather) {
            binding.date = totalWeather.date
            binding.temperature = showTemperature(totalWeather.minTemp, totalWeather.maxTemp)
            binding.stateAbbr = totalWeather.stateAbbr
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

private fun showTemperature(minTemp: Double, maxTemp: Double): String {
    return minTemp.roundToInt().toString().plus("°C / ").plus(maxTemp.roundToInt().toString())
        .plus("°C")
}