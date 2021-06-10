package com.wgp.niceweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.wgp.niceweather.logic.Repository
import com.wgp.niceweather.logic.model.Location

/**
 * 天气的界面ui的ViewModel
 */
class WeatherViewModel : ViewModel(){

    private val locationLiveData = MutableLiveData<Location>()
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData){
        Repository.refreshWeather(it.lng,it.lat)
    }

    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value = Location(lat,lng)
    }

}