package com.weather.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.R
import com.weather.app.model.CityResponse
import com.weather.app.model.UIItem
import com.weather.app.model.UIItem.Companion.HEADER_TYPE
import com.weather.app.model.UIItem.Companion.PLUS_BUTTON_TYPE
import com.weather.app.model.UIItem.Companion.TAPPED_TYPE
import com.weather.app.model.UIItem.Companion.TEXT_TYPE
import com.weather.app.model.UIItem.Companion.TITLE_TYPE
import kotlinx.android.synthetic.main.weather_detail_item_plus_button.view.*
import kotlinx.android.synthetic.main.weather_detail_item_text.view.*
import kotlinx.android.synthetic.main.weather_detail_item_title.view.*

/**
 * Adapter to display list of cities in the left panel
 */
class CitiesAdapter(private val action: Action? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Action {
        fun tap()
        fun tapAndAdd(uiItem: UIItem)
        fun tapAndDisplayScreen(uiItem: UIItem)
    }

    private var items: ArrayList<UIItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_TYPE -> TextViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_text,
                    parent,
                    false
                )
            )
            PLUS_BUTTON_TYPE -> PlusButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_plus_button,
                    parent,
                    false
                )
            )
            TITLE_TYPE -> TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_title,
                    parent,
                    false
                )
            )
            HEADER_TYPE -> HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_header,
                    parent,
                    false
                )
            )
            TAPPED_TYPE -> TappedTextViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.weather_detail_item_text,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Cannot render other type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (items[position].type) {
            TEXT_TYPE -> (holder as TextViewHolder).bind(items[position].text)
            PLUS_BUTTON_TYPE -> (holder as PlusButtonViewHolder).bind(action!!)
            TITLE_TYPE -> (holder as TitleViewHolder).bind(items[position], action!!)
            HEADER_TYPE -> holder as HeaderViewHolder
            TAPPED_TYPE -> (holder as TappedTextViewHolder).bind(items[position], action!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            0 -> TEXT_TYPE
            1 -> PLUS_BUTTON_TYPE
            2 -> TITLE_TYPE
            3 -> HEADER_TYPE
            4 -> TAPPED_TYPE
            else -> -1
        }
    }

    override fun getItemCount() = items.size

    fun addCity(uiItem: UIItem) {
        if (items.size > 0) {
            items.forEach {
                if (it.type == PLUS_BUTTON_TYPE) {
                    val position = items.indexOf(it)
                    items.add(position - 1, uiItem)
                    notifyDataSetChanged()
                }
            }
        } else {
            items.apply {
                clear()
                add(UIItem(HEADER_TYPE))
                add(uiItem)
                add(UIItem(PLUS_BUTTON_TYPE))
            }
            notifyDataSetChanged()
        }
    }

    fun addCityFromSearch(cityResponse: CityResponse?) {
        items.clear()
        if (cityResponse?.name != null) {
            items.add(
                UIItem(
                    TITLE_TYPE,
                    cityResponse.name.plus(", ${cityResponse.sys?.country}"),
                    id = cityResponse.id
                )
            )
        } else {
            items.add(UIItem(TEXT_TYPE, "City not found!"))
        }
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String?) = with(itemView) {
            tv_dateTime.text = text
        }
    }

    inner class PlusButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(action: Action) = with(itemView) {
            bt_add_city.setOnClickListener {
                action.tap()
            }
        }
    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(uiItem: UIItem, action: Action) = with(itemView) {
            tv_title.text = uiItem.text
            setOnClickListener {
                action.tapAndAdd(uiItem)
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TappedTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(uiItem: UIItem, action: Action) = with(itemView) {
            tv_dateTime.text = uiItem.text
            setOnClickListener {
                action.tapAndDisplayScreen(uiItem)
            }
        }
    }
}