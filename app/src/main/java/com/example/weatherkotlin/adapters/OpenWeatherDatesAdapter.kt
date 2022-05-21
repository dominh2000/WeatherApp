package com.example.weatherkotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.databinding.OpenWeatherItemBinding
import com.example.weatherkotlin.domain.OneDayForecast
import com.example.weatherkotlin.util.convertDateFromPattern5ToPattern4
import kotlin.math.roundToInt

class OpenWeatherDatesAdapter(private val onItemClicked: (OneDayForecast) -> Unit) :
    ListAdapter<OneDayForecast, OpenWeatherDatesAdapter.OpenWeatherDatesViewHolder>(
        OpenWeatherDiffCallback
    ) {

    companion object OpenWeatherDiffCallback : DiffUtil.ItemCallback<OneDayForecast>() {
        override fun areItemsTheSame(oldItem: OneDayForecast, newItem: OneDayForecast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OneDayForecast, newItem: OneDayForecast): Boolean {
            return oldItem == newItem
        }

    }

    class OpenWeatherDatesViewHolder(private var binding: OpenWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(openWeatherItem: OneDayForecast) {
            binding.apply {
                dateItem.text = openWeatherItem.dateTimeText.convertDateFromPattern5ToPattern4()
                temperatureItem.text =
                    openWeatherItem.mainForecastInfo.temp.roundToInt().toString().plus("°C")
                weatherState = openWeatherItem.weatherDescription.icon
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenWeatherDatesViewHolder {
        val itemViewHolder = OpenWeatherDatesViewHolder(
            OpenWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        itemViewHolder.itemView.setOnClickListener {
            val position = itemViewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: OpenWeatherDatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
