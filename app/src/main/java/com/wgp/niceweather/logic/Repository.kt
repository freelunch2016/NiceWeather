package com.wgp.niceweather.logic

import androidx.lifecycle.liveData
import com.wgp.niceweather.logic.model.Place
import com.wgp.niceweather.logic.network.NiceWeatherNetWork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层
 * 仓库层的主要工作就是判断调用方请求的数据
 * 应该从本地数据源中获取还是从网络数据源中获取
 * 并将获得的数据返回给调用方
 */
object Repository {

    fun searchPlaces(query:String)  =
        liveData(Dispatchers.IO){
                val result = try{
                    val searchPlaces = NiceWeatherNetWork.searchPlaces(query)
                    if (searchPlaces.status == "ok"){
                        val places = searchPlaces.places
                        Result.success(places)
                    }else{
                        Result.failure(RuntimeException("response status is ${searchPlaces.status}"))
                    }
                }catch (e:Exception){
                    Result.failure<List<Place>>(e)
                }

            emit(result)
    }

}





