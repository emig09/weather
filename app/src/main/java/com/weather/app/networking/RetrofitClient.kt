package com.weather.app.networking

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/"

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                    .client(provideOkHttp())
                    .baseUrl(OPEN_WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

    private fun provideOkHttp() = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()
}