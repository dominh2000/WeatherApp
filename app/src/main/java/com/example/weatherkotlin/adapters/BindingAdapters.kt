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
import com.example.weatherkotlin.viewmodels.WeatherApiStatus
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

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

@BindingAdapter("apiStatus", "contextImg")
fun bindApiStatus(statusImageView: ImageView, apiStatus: WeatherApiStatus?, context: Context?) {
    when (apiStatus) {
        WeatherApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        WeatherApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {
            statusImageView.visibility = View.GONE
            Snackbar.make(context!!, statusImageView, "Không có kết nối Internet hoặc máy chủ lỗi!", LENGTH_SHORT).show()
        }
    }
}
