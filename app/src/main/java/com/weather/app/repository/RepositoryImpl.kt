package com.weather.app.repository

import com.weather.app.R
import com.weather.app.networking.api.WeatherApi
import com.weather.app.networking.RetrofitClient
import com.weather.app.model.CityResponse
import com.weather.app.model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class RepositoryImpl(val result: OnResponse) : Repository {

    var weatherApi: WeatherApi? =
        RetrofitClient.retrofitInstance?.create(WeatherApi::class.java)

    override fun getCityByLocation(latitude: Double, longitude: Double) {
        weatherApi?.getWeatherDataByLocation(latitude, longitude)?.enqueue(object :
            Callback<WeatherResponse> {

            override fun onResponse(
                call: Call<WeatherResponse>?,
                response: Response<WeatherResponse>?
            ) {
                result.onResponseByCitySuccess(response?.body())
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) =
                result.onFailure(error(t))
        })
    }

    override fun getCityByName(cityName: String) {
        weatherApi?.getCitiesByName(cityName)?.enqueue(object : Callback<CityResponse> {

            override fun onResponse(call: Call<CityResponse>?, response: Response<CityResponse>?) {
                result.onResponseCityByName(response?.body())
            }

            override fun onFailure(call: Call<CityResponse>?, t: Throwable?) =
                result.onFailure(error(t))
        })
    }

    override fun getCityById(id: Int) {
        weatherApi?.getWeatherDataById(id)?.enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(
                call: Call<WeatherResponse>?, response: Response<WeatherResponse>?
            ) {
                result.onResponseCityById(response?.body())
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) =
                result.onFailure(error(t))
        })
    }

    private fun error(t: Throwable?): Int {
        return when (t) {
            is UnknownHostException -> R.string.main_no_connectivity
            else -> R.string.main_unknown_error
        }
    }
}