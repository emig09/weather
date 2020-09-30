package com.weather.app.repository

interface Repository {

    fun getCityByLocation(latitude: Double, longitude: Double)

    fun getCityByName(cityName: String)

    fun getCityById(id: Int)
}