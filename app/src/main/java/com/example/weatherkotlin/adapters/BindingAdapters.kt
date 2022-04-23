package com.example.weatherkotlin.adapters

import android.content.Context
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import com.example.weatherkotlin.network.TotalWeather

@BindingAdapter("listData")
fun bindRecyclerViewData(recyclerView: RecyclerView, data: List<TotalWeather>?) {
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
