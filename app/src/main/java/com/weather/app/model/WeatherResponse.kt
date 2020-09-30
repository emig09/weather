package com.weather.app.model

data class WeatherResponse(val city: City, val list: List<WeatherData>)