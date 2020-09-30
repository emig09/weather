package com.weather.app.repository

import com.weather.app.model.CityResponse
import com.weather.app.model.WeatherResponse

interface OnResponse {
    fun onResponseByCitySuccess(weatherResponse: WeatherResponse?)
    fun onResponseCityByName(cityResponse: CityResponse?)
    fun onResponseCityById(cityResponse: WeatherResponse?)
    fun onFailure(error: Int)
}