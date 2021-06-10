package com.wgp.niceweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.wgp.niceweather.NiceWeatherApplication
import com.wgp.niceweather.logic.model.Place

/**
 * 记录地址到本地
 */
object PlaceDao {

    private fun sharedPreference() =
        NiceWeatherApplication.context.getSharedPreferences("nice_weather", Context.MODE_PRIVATE)


    fun savePlace(place: Place) {
            sharedPreference().edit {
                putString("place",Gson().toJson(place))
            }
    }

    fun getSavedPlace():Place{
        val json = sharedPreference().getString("place", "")
        return Gson().fromJson(json,Place::class.java)
    }

    fun isPlaceSaved() = sharedPreference().contains("place")
}
