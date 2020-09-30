package com.weather.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.weather.app.R
import com.weather.app.adapter.WeatherDetailAdapter
import com.weather.app.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_detail_fragment.*

/**
 * Displays city name, datetime and temperature for an specific city
 */
class WeatherDetailFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private val adapter: WeatherDetailAdapter by lazy { WeatherDetailAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this).get(WeatherViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.weatherResponseByCityId.observe(this, Observer {
            it?.let { adapter.loadDetail(it) }
        })

        viewModel.weatherResponseByCity.observe(this, Observer {
            it?.let { adapter.loadDetail(it) }
        })

        viewModel.errors.observe(this, Observer {
            showSnackBar(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.weather_detail_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list.adapter = adapter
    }

    private fun showSnackBar(textToShow: Int) =
        Snackbar.make(view!!, textToShow, Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                ContextCompat.getColor(
                    activity!!,
                    android.R.color.holo_red_dark
                )
            )
            .show()
}