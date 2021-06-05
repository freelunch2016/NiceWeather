package com.wgp.niceweather.logic.network

import com.wgp.niceweather.NiceWeatherApplication
import com.wgp.niceweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface PlaceService {

    /**
     * 查询地址位置信息
     */
    @GET("v2/place?token=${NiceWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String) :Call<PlaceResponse>
}