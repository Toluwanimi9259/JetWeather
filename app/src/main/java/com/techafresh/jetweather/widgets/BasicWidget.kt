package com.techafresh.jetweather.widgets

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.AppWidgetId
import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.DynamicThemeColorProviders.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.techafresh.jetweather.R
import com.techafresh.jetweather.api.WeatherAPI
import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.repository.WeatherRepo
import com.techafresh.jetweather.util.WeatherWrapper
import com.techafresh.jetweather.util.formatDecimals
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BasicWidget(private val weatherAPI: WeatherAPI) : GlanceAppWidget() {
    companion object {
        private val smallMode = DpSize(140.dp, 140.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 260.dp)
    }
    @SuppressLint("CoroutineCreationDuringComposition")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val isError = remember { mutableStateOf(false) }
            val data = remember { mutableStateOf<Weather?>(null) }
            val size = LocalSize.current

            val sharedPreferences = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE)
            val city = sharedPreferences.getString("location", "California")


            LaunchedEffect(key1 = Unit) {
                try {
                    data.value = weatherAPI.getForecast(city!!, "imperial")
                } catch (ex: Exception) {
                    isError.value = true
                }
            }


                if (isError.value) {
                    ErrorWidget()
                } else {
                    when(size){
                        smallMode -> WeatherSmall(data = data.value)
                        mediumMode -> WeatherMedium(data = data.value)
                        largeMode -> WeatherLarge(data = data.value)
                    }
                }

        }
    }

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(setOf(smallMode, mediumMode, largeMode))

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        val rv = RemoteViews(context.packageName, R.layout.error_layout_file)
        rv.setTextViewText(
            R.id.error_text_view,
            "Error was thrown. \nThis is a custom view \nError Message: `${throwable.message}`"
        )
//        rv.setOnClickPendingIntent(R.id.error_icon, getErrorIntent(context, throwable))
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rv)

    }
}

@Composable
fun WeatherSmall(data: Weather?){
    Column(
        modifier = GlanceModifier.fillMaxSize().cornerRadius(12.dp).padding(16.dp).background(ImageProvider(R.drawable.widgetbackground)),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data != null){
            Row(
                modifier = GlanceModifier.wrapContentHeight().fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                WeatherIcon(data.list[0], modifier = GlanceModifier.fillMaxWidth().defaultWeight())
                PlaceWeather(data, modifier = GlanceModifier.fillMaxWidth().defaultWeight())
            }
            CurrentTemperature(data.list[0], modifier = GlanceModifier.fillMaxSize(), Alignment.Start)
        }else{
            ErrorWidget()
        }
    }
}

@Composable
fun WeatherMedium(data: Weather?){
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(16.dp).background(ImageProvider(R.drawable.widgetbackground)).cornerRadius(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data != null){
            Row(
                modifier = GlanceModifier.wrapContentHeight().fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                WeatherIcon(data.list[0], modifier = GlanceModifier.fillMaxWidth().defaultWeight())
                PlaceWeather(data, modifier = GlanceModifier.fillMaxWidth().defaultWeight())
            }
            Row(
                modifier = GlanceModifier.wrapContentHeight().fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                CurrentTemperature(
                    data.list[0],
                    modifier = GlanceModifier.fillMaxHeight(),
                    Alignment.Start,
                )
                OtherDetails(data.list[0], modifier = GlanceModifier.fillMaxSize())
            }
        }else{
            ErrorWidget()
        }
    }
}

@Composable
fun WeatherLarge(data : Weather?){
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(16.dp)  .background(ImageProvider(R.drawable.widgetbackground))  .cornerRadius(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data != null){
            Row(
                modifier = GlanceModifier.wrapContentHeight().fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                WeatherIcon(data.list[0], modifier = GlanceModifier.fillMaxWidth().defaultWeight())
                PlaceWeather(data, modifier = GlanceModifier.fillMaxWidth().defaultWeight())
            }
            Row(
                modifier = GlanceModifier.wrapContentHeight().fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                CurrentTemperature(
                    data.list[0],
                    modifier = GlanceModifier.wrapContentHeight(),
                    Alignment.Start,
                )
                OtherDetails(data.list[0], modifier = GlanceModifier.fillMaxWidth())
            }
            Spacer(GlanceModifier.size(8.dp))
            DailyForecast(data)
        }else{
            ErrorWidget()
        }
    }
}

@Composable
fun ErrorWidget(){
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(8.dp) .background(ImageProvider(R.drawable.widgetbackground))
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(color = ColorProvider(Color.Green), modifier = GlanceModifier.fillMaxWidth())
    }
}

@AndroidEntryPoint
class BasicWidgetReceiver : GlanceAppWidgetReceiver(){

    @Inject
    lateinit var weatherAPI: WeatherAPI
    override val glanceAppWidget: GlanceAppWidget
//        get() = PreviewWidget
        get() = BasicWidget(weatherAPI)
}