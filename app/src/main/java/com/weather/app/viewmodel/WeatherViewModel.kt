package com.weather.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weather.app.model.CityResponse
import com.weather.app.model.UIItem
import com.weather.app.model.WeatherResponse
import com.weather.app.repository.OnResponse
import com.weather.app.repository.Repository
import com.weather.app.repository.RepositoryImpl

class WeatherViewModel : ViewModel(), OnResponse {

    private val repository: Repository = RepositoryImpl(this)

    val weatherResponseByCity = MutableLiveData<WeatherResponse>()
    val weatherResponseByCityId = MutableLiveData<WeatherResponse>()
    val weatherResponseByCityName = MutableLiveData<CityResponse>()
    val cityAddedToList = MutableLiveData<UIItem>()

    val errors = MutableLiveData<Int>()

    /**
     * Returns weather given the lat-long provided (the one got at the time user opens the app)
     */
    fun getCityByLocation(latitude: Double, longitude: Double) {
        repository.getCityByLocation(latitude, longitude)
    }

    /**
     * Return cities given a query when user introduce it in the search view
     */
    fun getCityByName(cityName: String) = repository.getCityByName(cityName)

    /**
     * Returns 5 day weather for a specific city, given an id
     */
    fun getCityById(id: Int) = repository.getCityById(id)

    override fun onResponseByCitySuccess(weatherResponse: WeatherResponse?) {
        weatherResponseByCity.value = weatherResponse
    }

    override fun onResponseCityByName(cityResponse: CityResponse?) {
        weatherResponseByCityName.value = cityResponse
    }

    override fun onResponseCityById(cityResponse: WeatherResponse?) {
        weatherResponseByCityId.value = cityResponse
    }

    override fun onFailure(error: Int) {
        errors.value = error
    }

    fun addCityToList(uiItem: UIItem?) {
        cityAddedToList.value = uiItem
    }
}