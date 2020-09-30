package com.weather.app.model

data class City(val id: Int, val name: String, val country: String) {

    fun getCityAndName() = name.plus(", $country")
}