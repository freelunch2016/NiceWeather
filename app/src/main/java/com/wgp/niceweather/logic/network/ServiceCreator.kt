package com.wgp.niceweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 定义Retrofit构造器
 * 单例
 */
object ServiceCreator {

    private const val BASE_URL = "https://api.caiyunapp.com/"

    //创建Retrofit对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()


    fun <T> create(service:Class<T>) = retrofit.create(service)

    //利用kotlin中的泛型的高级特性
    inline  fun <reified T> create() = create<T>(T::class.java)

}