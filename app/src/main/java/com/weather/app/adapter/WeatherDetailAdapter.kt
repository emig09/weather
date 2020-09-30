package com.weather.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.app.R
import com.weather.app.model.UIItem
import com.weather.app.model.UIItem.Companion.TEXT_TYPE
import com.weather.app.model.UIItem.Companion.TITLE_TYPE
import com.weather.app.model.WeatherData
import com.weather.app.model.WeatherResponse
import kotlinx.android.synthetic.main.weather_detail_item_text.view.*
import kotlinx.android.synthetic.main.weather_detail_item_title.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter to display detail of date times and temperature
 */
class WeatherDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ORIGINAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val TARGET_DATE_FORMAT = "dd MMMM yyyy h:mm a"
    }

    private var items: ArrayList<UIItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_TYPE -> TextViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_text,
                    parent,
                    false
                )
            )
            TITLE_TYPE -> TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_title,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Cannot render other type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].type) {
            TEXT_TYPE -> (holder as TextViewHolder).bind(items[position].weatherData)
            TITLE_TYPE -> (holder as TitleViewHolder).bind(items[position].text)
        }
    }

    override fun getItemViewType(position: Int) =
        when (items[position].type) {
            0 -> TEXT_TYPE
            2 -> TITLE_TYPE
            else -> -1
        }

    override fun getItemCount() = items.size

    fun loadDetail(weatherResponse: WeatherResponse) {
        items.clear()
        items.add(
            UIItem(
                TITLE_TYPE,
                weatherResponse.city.name.plus(" (${weatherResponse.city.country})")
            )
        )

        weatherResponse.list.forEach {
            items.add(UIItem(TEXT_TYPE, weatherData = it))
        }
        notifyDataSetChanged()
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weatherData: WeatherData?) = with(itemView) {
            weatherData?.let {
                tv_dateTime.text = formatDate(it.dateTime)
                tv_temperature.text = fahrenheit(it.main.temp)
                val iconUrl =
                    "http://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png"

                Glide.with(this)
                    .load(iconUrl)
                    .circleCrop()
                    .into(iv_weather_icon)
            }
        }
    }

    private fun formatDate(dateTime: String): String {
        var desiredDate = dateTime
        val originalFormat = SimpleDateFormat(ORIGINAL_DATE_FORMAT, Locale.getDefault())
        val targetFormat = SimpleDateFormat(TARGET_DATE_FORMAT, Locale.getDefault())
        originalFormat.parse(dateTime)?.let {
            desiredDate = targetFormat.format(it)
        }
        return desiredDate
    }

    private fun fahrenheit(fahrenheit: Double) = fahrenheit.toString().plus(" ºf")

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(title: String?) = with(itemView) {
            tv_title.text = title
        }
    }
}