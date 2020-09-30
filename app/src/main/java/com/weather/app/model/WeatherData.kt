package com.weather.app.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val main: Main,
    val weather: List<Weather>,
    val clouds: Cloud,
    val wind: Wind,
    @SerializedName("dt_txt") val dateTime: String
)