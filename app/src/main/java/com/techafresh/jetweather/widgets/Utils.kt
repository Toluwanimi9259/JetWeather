package com.techafresh.jetweather.widgets

import android.R.attr.src
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.techafresh.jetweather.R
import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.model.WeatherItem
import com.techafresh.jetweather.util.formatDate
import com.techafresh.jetweather.util.formatDecimals
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun WeatherIcon(weatherInfo: WeatherItem, modifier: GlanceModifier = GlanceModifier) {
    Box(modifier = modifier, contentAlignment = Alignment.TopStart) {
        Image(
            provider = ImageProvider(formatIcon(weatherInfo.weather[0].icon)),
            contentDescription = null,
            modifier = GlanceModifier.size(48.dp),
//            colorFilter = ColorFilter.tint(ColorProvider(Color(0xFF04CDFF)))
        )
    }
}

@Composable
fun CurrentTemperature(
    weatherInfo: WeatherItem,
    modifier: GlanceModifier = GlanceModifier,
    horizontal: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = horizontal,
    ) {
        val defaultWeight = GlanceModifier.wrapContentSize()
        Text(
            text = "${formatDecimals(weatherInfo.main.temp)}°",
            style = TextStyle(
                fontSize = 48.sp,
                color = ColorProvider(Color.White)
            ),
            modifier = defaultWeight,
        )
        Row(modifier = defaultWeight) {
            Text(
                text = "${formatDecimals(weatherInfo.main.temp_min)}°",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color.White)
                ),
            )
            Spacer(GlanceModifier.size(8.dp))
            Text(
                text = "${formatDecimals(weatherInfo.main.temp_max)}º",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Color.White)
                ),
            )
        }
    }
}

@Composable
fun PlaceWeather(
    weatherInfo: Weather,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.End,
    ) {
        val defaultWeight = GlanceModifier.defaultWeight()
        Text(
            text = weatherInfo.city.name,
            style = TextStyle(
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                color = ColorProvider(Color.White)
            ),
            modifier = defaultWeight,
        )

        Spacer(modifier = GlanceModifier.height(5.dp))
        Text(
            text = weatherInfo.list[0].weather[0].description,
            style = TextStyle(
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                color = ColorProvider(Color.White)
            ),
            modifier = defaultWeight,
        )
    }
}


@Composable
fun OtherDetails(
    weatherInfo: WeatherItem,
    modifier: GlanceModifier = GlanceModifier,
) {
    Row(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OtherDetail(image = R.drawable.humidity, value = weatherInfo.main.humidity.toString() + "%", detail = "Humidity")
        OtherDetail(image = R.drawable.pressure, value = "${weatherInfo.main.pressure} psi", detail = "Pressure")
        OtherDetail(image = R.drawable.wind, value = formatDecimals(weatherInfo.wind.speed) + " mph", detail = "Wind")
    }
}

@Composable
fun OtherDetail(image : Int, value : String, detail : String){
    Column(
        modifier = GlanceModifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            provider = ImageProvider(image),
            contentDescription = detail,
            modifier = GlanceModifier.size(24.dp),
            colorFilter = ColorFilter.tint(ColorProvider(Color(0xFF0DA9CF)))
        )

        Text(
            text = value,
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(Color.White)
            ),
        )

        Text(
            text = detail,
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(Color.White)
            ),
        )
    }
}

@Composable
fun DailyForecast(weatherInfo: Weather) {

    Column {
        weatherInfo.list.filter {
            it.dt_txt.substring(11) == "00:00:00" // Get rid of duplicate values
        }.forEach { weatherItem ->
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(8.dp).background(Color.DarkGray.copy(.4f)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatDate(weatherItem.dt).split(",")[0],
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(Color.White)
                    ),
                )
                Row(
                    modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                    horizontalAlignment = Alignment.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = ColorProvider(Color.White)
                    )
                    Image(
                        provider = ImageProvider(formatIcon(weatherItem.weather[0].icon)),
                        contentDescription = null,
                        modifier = GlanceModifier.size(24.dp).padding(4.dp),
//                        colorFilter = ColorFilter.tint(ColorProvider(Color.Yellow))
                    )
                    Text(
                        text = "${formatDecimals(weatherItem.main.temp_min)}º",
                        style = textStyle,
                        modifier = GlanceModifier.padding(4.dp),
                    )
                    Text(
                        text = "${formatDecimals(weatherItem.main.temp_max)}º",
                        style = textStyle,
                        modifier = GlanceModifier.padding(4.dp),
                    )
                }
            }
        }
    }
}

fun formatIcon(weatherCode : String) : Int{
    return when (weatherCode){
        "01d" -> R.drawable.day_1
        "01n" -> R.drawable.night_1
        "02d" -> R.drawable.day_2
        "02n" -> R.drawable.night_2
        "03d" -> R.drawable.night_3
        "03n" -> R.drawable.night_3
        "04d" -> R.drawable.day_4
        "04n" -> R.drawable.day_4
        "09d" -> R.drawable.day_9
        "09n" -> R.drawable.day_9
        "10d" -> R.drawable.day_10
        "10n" -> R.drawable.night_10
        "11d" -> R.drawable.night_11
        "11n" -> R.drawable.night_11
        "13d" -> R.drawable.night_13
        "13n" -> R.drawable.night_13
        "50d" -> R.drawable.night_50
        "50n" -> R.drawable.night_50
        else -> {
            R.drawable.sunset
        }
    }
}