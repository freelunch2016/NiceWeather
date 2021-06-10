package com.wgp.niceweather.logic

import androidx.lifecycle.liveData
import com.wgp.niceweather.logic.dao.PlaceDao
import com.wgp.niceweather.logic.model.Place
import com.wgp.niceweather.logic.model.Weather
import com.wgp.niceweather.logic.network.NiceWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层
 * 仓库层的主要工作就是判断调用方请求的数据
 * 应该从本地数据源中获取还是从网络数据源中获取
 * 并将获得的数据返回给调用方
 */
object Repository {

    fun searchPlaces(query: String) =
        liveData(Dispatchers.IO) {
            val result = try {
                val searchPlaces = NiceWeatherNetWork.searchPlaces(query)
                if (searchPlaces.status == "ok") {
                    val places = searchPlaces.places
                    Result.success(places)
                } else {
                    Result.failure(RuntimeException("response status is ${searchPlaces.status}"))
                }
            } catch (e: Exception) {
                Result.failure<List<Place>>(e)
            }

            emit(result)
        }

    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val realtimeWeather = async {
                    NiceWeatherNetWork.getRealtimeWeather(lng, lat)
                }
                val dailyWeather = async {
                    NiceWeatherNetWork.getDailyWeather(lng, lat)
                }
                val realtimeResponse = realtimeWeather.await()
                val dailyResponse = dailyWeather.await()

                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather =
                        Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}," +
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }

        } catch (e: Exception) {
            Result.failure<Weather>(e)
        }
        emit(result)
    }

    /**
     * try catch统一抓起简化上述异常处理的代码，我没有替换
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }


    fun  savePlace(place: Place) = PlaceDao.savePlace(place)
    fun  getSavedPlace() = PlaceDao.getSavedPlace()
    fun  isPlaceSaved() = PlaceDao.isPlaceSaved()
}





