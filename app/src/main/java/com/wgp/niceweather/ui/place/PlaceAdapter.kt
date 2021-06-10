package com.wgp.niceweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wgp.niceweather.R
import com.wgp.niceweather.logic.model.Place
import com.wgp.niceweather.logic.model.Weather
import com.wgp.niceweather.ui.weather.WeatherActivity


class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName = view.findViewById<TextView>(R.id.placeName)
        val placeAddress = view.findViewById<TextView>(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val viewHolder = ViewHolder(item)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            val place = placeList[position]
            val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra("location_lat",place.location.lat)
                putExtra("location_lng",place.location.lng)
                putExtra("place_name",place.name)
            }
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}