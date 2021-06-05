package com.wgp.niceweather.logic.model

import com.google.gson.annotations.SerializedName


data class PlaceResponse(val status:String,val places:List<Place>)

data class Place(val name:String,val location:Location,
                 @SerializedName("formatted_address") val address:String)

/**
 * lat 纬度
 * lng 经度
 */
data class Location(val lat:String,val lng:String )