package com.weather.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.weather.app.adapter.CitiesAdapter
import com.weather.app.fragment.AddCityFragment
import com.weather.app.fragment.WeatherDetailFragment
import com.weather.app.model.UIItem
import com.weather.app.model.UIItem.Companion.TAPPED_TYPE
import com.weather.app.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CitiesAdapter.Action {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        viewModel.weatherResponseByCity.observe(this, Observer {
            it?.run {
                showLoading(false)
                adapter.addCity(
                    UIItem(
                        TAPPED_TYPE,
                        it.city.getCityAndName(),
                        id = it.city.id
                    )
                )
            }
        })

        viewModel.cityAddedToList.observe(this, Observer {
            supportFragmentManager.popBackStack()
            it?.run {
                adapter.addCity(it.copy(type = TAPPED_TYPE))
                viewModel.getCityById(it.id!!)
            }
        })

        checkLocationPermission()

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        drawerLayout = findViewById(R.id.drawer_layout)

        adapter = CitiesAdapter(this)
        recycler.adapter = adapter
        adapter.clearItems()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }

        if (savedInstanceState == null) {
            showWeatherDetailFragment()
        }
    }

    private fun showWeatherDetailFragment() =
        supportFragmentManager
            .beginTransaction()
            .add(R.id.content_frame, WeatherDetailFragment())
            .commit()

    override fun tap() {
        drawerLayout.closeDrawers()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, AddCityFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun tapAndAdd(uiItem: UIItem) = adapter.addCity(uiItem)

    override fun tapAndDisplayScreen(uiItem: UIItem) {
        drawerLayout.closeDrawers()
        showLoading(true)
        viewModel.getCityById(uiItem.id!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                getWeatherGivenALocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION
                )
            }
        } else {
            getLastKnownLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_COARSE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission granted
                    getLastKnownLocation()
                } else {
                    // permission denied
                    getWeatherGivenALocation()
                }
                return
            }
        }
    }

    private fun getLastKnownLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationProvider: String = LocationManager.NETWORK_PROVIDER
        val lastKnownLocation: Location? = locationManager.getLastKnownLocation(locationProvider)

        lastKnownLocation?.let {
            viewModel.getCityByLocation(it.latitude, it.longitude)
        } ?: run {
            getWeatherGivenALocation()
        }
    }

    private fun getWeatherGivenALocation() {
        showNoLocationSnackBar()
        viewModel.getCityByLocation(-31.416668, -64.183334)
    }

    private fun showNoLocationSnackBar() =
        Snackbar.make(
            findViewById(android.R.id.content),
            R.string.main_not_location_found,
            Snackbar.LENGTH_LONG
        ).show()

    private fun showLoading(visible: Boolean) {
        loading_view.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
