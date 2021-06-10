package com.wgp.niceweather.logic.model

/**
 * 把实时天气和未来几天天气封装到一起
 */
class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily) {

}