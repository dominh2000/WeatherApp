package com.example.weatherkotlin.adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.example.weatherkotlin.R
import com.example.weatherkotlin.domain.WeatherOneDay
import com.example.weatherkotlin.network.TotalWeather
import com.example.weatherkotlin.viewmodels.WeatherApiStatus

@BindingAdapter("listData")
fun bindRecyclerViewData(recyclerView: RecyclerView, data: List<WeatherOneDay>?) {
    val adapter = recyclerView.adapter as WeatherDatesAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUri", "contextImg")
fun bindWeatherStateImage(imageView: ImageView, imgUri: String?, context: Context?) {
    val baseUrl = "https://www.metaweather.com/static/img/weather/"
    val imageLoader = ImageLoader.Builder(context!!)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    imgUri?.let {
        val imageUri = baseUrl.plus(imgUri).plus(".svg").toUri().buildUpon().scheme("https").build()
        imageView.load(imageUri, imageLoader) {

        }
    }
}

@BindingAdapter("apiStatus")
fun bindApiStatus(statusImageView: ImageView, apiStatus: WeatherApiStatus?) {
    when (apiStatus) {
        WeatherApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        WeatherApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}
