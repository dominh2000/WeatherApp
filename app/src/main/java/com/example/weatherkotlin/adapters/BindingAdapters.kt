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
import com.example.weatherkotlin.const.weatherIconMap
import com.example.weatherkotlin.domain.OneDayForecast
import com.example.weatherkotlin.domain.WeatherOneDay
import com.example.weatherkotlin.viewmodels.OpenWeatherApiStatus
import com.example.weatherkotlin.viewmodels.WeatherApiStatus
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

/*
    For MetaWeather API
 */
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
        WeatherApiStatus.ERROR -> {
            statusImageView.visibility = View.GONE
            Snackbar.make(
                context!!,
                statusImageView,
                "Kh??ng c?? k???t n???i Internet ho???c m??y ch??? l???i!",
                LENGTH_SHORT
            ).show()
        }
        else -> { }
    }
}

/*
    For OpenWeather API
 */
@BindingAdapter("listDataOW")
fun bindRecyclerViewOWData(recyclerView: RecyclerView, data: List<OneDayForecast>?) {
    val adapter = recyclerView.adapter as OpenWeatherDatesAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUriOW", "contextImg")
fun bindOWWeatherStateImage(imageView: ImageView, imgUri: String?, context: Context?) {
    val baseUrl = "https://api.met.no/images/weathericons/svg/"
    val imageLoader = ImageLoader.Builder(context!!)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    imgUri?.let {
        val imageUri =
            baseUrl.plus(weatherIconMap[it]).plus(".svg").toUri().buildUpon().scheme("https")
                .build()
        imageView.load(imageUri, imageLoader) {
        }
    }
}

@BindingAdapter("apiOWStatus", "contextImg")
fun bindOWApiStatus(
    statusImageView: ImageView,
    apiStatus: OpenWeatherApiStatus?,
    context: Context?
) {
    when (apiStatus) {
        OpenWeatherApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        OpenWeatherApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        OpenWeatherApiStatus.ERROR -> {
            statusImageView.visibility = View.GONE
            Snackbar.make(
                context!!,
                statusImageView,
                "???? x???y ra l???i k???t n???i. H??y ki???m tra k???t n???i Internet c???a b???n!",
                LENGTH_SHORT
            )
                .setAnchorView(R.id.bottom_nav)
                .show()
        }
        else -> { }
    }
}
