package com.wgp.niceweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wgp.niceweather.R
import com.wgp.niceweather.logic.model.Weather
import com.wgp.niceweather.logic.model.getSky
import java.util.*

/**
 * 显示天气详情界面
 */
class WeatherActivity : AppCompatActivity() {

     val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    private lateinit var swipeRefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        swipeRefresh = findViewById(R.id.swipeRefresh)

        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lat")?:""
        }
        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra("location_lng")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name")?:""
        }


        viewModel.weatherLiveData.observe(this, {
            val weather = it.getOrNull()
            if (weather != null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        val navBtn = findViewById<Button>(R.id.navBtn)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        
        navBtn.setOnClickListener { 
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener( object: DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)

            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
    }

     fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        swipeRefresh.isRefreshing =true
    }

    /**
     * 执行显示ui信息
     */
    private fun showWeatherInfo(weather: Weather) {
        val placeName = findViewById<TextView>(R.id.placeName)
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        //填充now.xml布局中的实时天气数据
        val currentTempText = "${realtime.temperature.toInt()}℃"
        val currentTemp = findViewById<TextView>(R.id.currentTemp)
        currentTemp.text = currentTempText
        val currentSky = findViewById<TextView>(R.id.currentSky)
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数${realtime.airQuality.aqi.chn.toInt()}"
        val currentAQI = findViewById<TextView>(R.id.currentAQI)
        currentAQI.text = currentPM25Text
        val nowLayout = findViewById<RelativeLayout>(R.id.nowLayout)
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)


        //填充forecast.xml布局中的数据，未来天气预报
        val forecastLayout = findViewById<LinearLayout>(R.id.forecastLayout)
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view  = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)

            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky =  getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text =sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()}"
            temperatureInfo.text = tempText

            forecastLayout.addView(view)
        }

        //填充life_index.xml布局 生活指数相关
        val lifeIndex = daily.lifeIndex
        val coldRiskText = findViewById<TextView>(R.id.coldRiskText)
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        val dressingText = findViewById<TextView>(R.id.dressingText)
        dressingText.text = lifeIndex.dressing[0].desc
        val ultravioletText = findViewById<TextView>(R.id.ultravioletText)
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        val carWashingText = findViewById<TextView>(R.id.carWashingText)
        carWashingText.text = lifeIndex.carWashing[0].desc
        findViewById<ScrollView>(R.id.weatherLayout).visibility = View.VISIBLE
    }


}

