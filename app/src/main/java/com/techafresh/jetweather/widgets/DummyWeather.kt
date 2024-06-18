package com.techafresh.jetweather.widgets

import com.techafresh.jetweather.model.City
import com.techafresh.jetweather.model.Clouds
import com.techafresh.jetweather.model.Coord
import com.techafresh.jetweather.model.Main
import com.techafresh.jetweather.model.Rain
import com.techafresh.jetweather.model.Snow
import com.techafresh.jetweather.model.Sys
import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.model.WeatherItem
import com.techafresh.jetweather.model.WeatherX
import com.techafresh.jetweather.model.Wind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun provideWeather() : Weather {
    return withContext(Dispatchers.IO) {
        Weather(
            city= City(coord= Coord(lat=32.7153, lon=-117.1573), country="US", id=5391811, name="San Diego", population=1307402, sunrise=1718541632, sunset=1718593108, timezone=-25200),
            cnt = 40,
            cod = "200",
            list= listOf(WeatherItem(
                clouds= Clouds(all=59),
                dt=1718571600,
                dt_txt="2024-06-16 21:00:00",
                main = Main(feels_like=69.94, grnd_level=1004, humidity=69, pressure=1009, sea_level=1009, temp=70.0, temp_kf=1.69, temp_max=70.0, temp_min=66.96),
                pop=0.0,
                rain= Rain(1.0),
                snow= Snow(2.3),
                sys= Sys(pod="pod"),
                visibility=10000,
                weather = listOf(WeatherX(
                    description="broken clouds",
                    icon="04d",
                    id=803,
                    main="Clouds"
                )),
                wind= Wind(deg=212, gust=11.77, speed=11.7))),
            message=2)


    }
}

fun provideWeatherAgain() : Weather{
    return  Weather(
        city= City(coord= Coord(lat=32.7153, lon=-117.1573), country="US", id=5391811, name="San Diego", population=1307402, sunrise=1718541632, sunset=1718593108, timezone=-25200),
        cnt = 40,
        cod = "200",
        list= listOf(WeatherItem(
            clouds= Clouds(all=59),
            dt=1718571600,
            dt_txt="2024-06-16 21:00:00",
            main = Main(feels_like=69.94, grnd_level=1004, humidity=69, pressure=1009, sea_level=1009, temp=70.0, temp_kf=1.69, temp_max=70.0, temp_min=66.96),
            pop=0.0,
            rain= Rain(1.0),
            snow= Snow(2.3),
            sys= Sys(pod="pod"),
            visibility=10000,
            weather = listOf(WeatherX(
                description="broken clouds",
                icon="04d",
                id=803,
                main="Clouds"
            )),
            wind= Wind(deg=212, gust=11.77, speed=11.7))),
        message=2)


}