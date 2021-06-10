package com.wgp.niceweather.logic.network

import com.wgp.niceweather.NiceWeatherApplication
import com.wgp.niceweather.logic.model.DailyResponse
import com.wgp.niceweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface WeatherService {

    @GET("v2.5/${NiceWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    @GET("v2.5/${NiceWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>

}