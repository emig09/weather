package com.weather.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.weather.app.R
import com.weather.app.adapter.CitiesAdapter
import com.weather.app.model.UIItem
import com.weather.app.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_detail_fragment.*

/**
 * Displays screen with search after user clicks on add city in the right panel
 */
class AddCityFragment : Fragment(), CitiesAdapter.Action {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(WeatherViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.weatherResponseByCityName.observe(this, Observer {
            showLoading(false)
            adapter.addCityFromSearch(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CitiesAdapter(this)
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.clearItems()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchMenu = menu.findItem(R.id.action_search)
        searchMenu.expandActionView()

        (searchMenu.actionView as SearchView).apply {
            queryHint = resources.getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        adapter.clearItems()
                        closeKeyboard()
                        showLoading(true)
                        viewModel.getCityByName(it)
                    }
                    return true
                }

                override fun onQueryTextChange(query: String?) = false
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showLoading(visible: Boolean) {
        loading_view.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun closeKeyboard() {
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    override fun tap() = Unit

    override fun tapAndAdd(uiItem: UIItem) = viewModel.addCityToList(uiItem)

    override fun tapAndDisplayScreen(uiItem: UIItem) = Unit
}