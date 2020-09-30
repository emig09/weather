package com.weather.app.networking.api

import com.weather.app.model.CityResponse
import com.weather.app.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        private const val APP_ID = "2eadbcbb534c640baf3e2dc67aa222f3"
    }

    @GET("data/2.5/forecast")
    fun getWeatherDataByLocation(@Query("lat") latitude: Double,
                                 @Query("lon") longitude: Double,
                                 @Query("unit") unit: String? = "metric",
                                 @Query("appid") appId: String = APP_ID): Call<WeatherResponse>

    @GET("data/2.5/weather")
    fun getCitiesByName(@Query("q") cityName: String,
                        @Query("appid") appId: String = APP_ID): Call<CityResponse>

    @GET("data/2.5/forecast")
    fun getWeatherDataById(@Query("id") cityId: Int,
                           @Query("appid") appId: String = APP_ID): Call<WeatherResponse>
}