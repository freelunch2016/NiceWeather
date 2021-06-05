package com.wgp.niceweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


/**
 * 提供一种全局获取Context的方式
 */
class NiceWeatherApplication : Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context

        //彩云天气的令牌
        const val  TOKEN = "pIe1zZfAVUuw0zpB"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}