package com.techafresh.jetweather.screens.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.techafresh.jetweather.R
import com.techafresh.jetweather.components.HumidityPressureRow
import com.techafresh.jetweather.components.SunsetSunRiseRow
import com.techafresh.jetweather.components.WeatherDetailRow
import com.techafresh.jetweather.model.Favourite
import com.techafresh.jetweather.model.Weather
import com.techafresh.jetweather.navigation.Screens
import com.techafresh.jetweather.util.WeatherWrapper
import com.techafresh.jetweather.util.formatDate
import com.techafresh.jetweather.util.formatDecimals
import com.techafresh.jetweather.viewmodel.FavViewModel
import com.techafresh.jetweather.viewmodel.SettingsViewModel
import com.techafresh.jetweather.viewmodel.WeatherViewModel


@SuppressLint("SuspiciousIndentation")
@Composable
fun MainScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    settingsViewModel : SettingsViewModel = hiltViewModel(),
    city: String?
){
    Log.d("CITY", "MainScreen: $city")

    val unitFromDb = settingsViewModel.unitList.collectAsState().value
    var unit by remember {
        mutableStateOf("imperial")
    }
    var isImperial by remember {
        mutableStateOf(false)
    }

    if (unitFromDb.isNotEmpty()){
        unit = unitFromDb[0].unit.split(" ")[0].lowercase()
        isImperial = unit == "imperial"

        val weatherData = produceState<WeatherWrapper<Weather , Boolean , Exception>>(
            initialValue = WeatherWrapper(loading = true)
        ){
            value = weatherViewModel.getWeatherData(city.toString(), units = unit)
        }.value

        if (weatherData.loading == true){
            LinearProgressIndicator()
        }else {
            MainScaffold(weatherData.data!! , navController)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(weather: Weather , navController: NavController){

    val isFavClicked = rememberSaveable{ mutableStateOf(false) }
    Scaffold(
        topBar = {
            WeatherAppBar(
                appBarTitle = weather.city.name + ", ${weather.city.country}",
                icon = if (isFavClicked.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                navController = navController,
                onActionClicked = {
                 navController.navigate(Screens.SearchScreen.name)
                },
                onButtonClicked = {
//                    isFavClicked.value = !isFavClicked.value
//                    if (isFavClicked.value){
//                        favViewModel.addToFav(Favourite(weather.city.name , weather.city.country))
//                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            MainContent(data = weather)
        }
    }
}

@Composable
fun MainContent(data : Weather){
    val weatherItem = data.list[0]

    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}@2x.png"
    val imageUrl2 = "https://openweathermap.org/img/wn/10d@2x.png"
    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text(
            text = formatDate(weatherItem.dt),
            modifier = Modifier.padding(6.dp),
            fontWeight = FontWeight.Medium
        )

        Surface(
            modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFFFC400)
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                WeatherStateImage(imageUrl = imageUrl)
                Text(text = formatDecimals(weatherItem.main.temp)+"º",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold)
                Text(text = weatherItem.weather[0].main,
                    fontStyle = Italic)
            }
        }
        
        HumidityPressureRow(weatherItem = weatherItem, isImperial = true)
        Divider()
        SunsetSunRiseRow(weather = data)
        Text(
            text = "This Week",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Surface(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            color = Color(0xFFEEF1EF),
            shape = RoundedCornerShape(size = 14.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(2.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
               items(data.list.filter {
                   it.dt_txt.substring(11) == "00:00:00"
               }){
                   WeatherDetailRow(weather = it)
               }

            }
        }
    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    AsyncImage(
        modifier = Modifier.size(80.dp),
        model = imageUrl,
        contentDescription = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun WeatherAppBar(
    appBarTitle : String = "Ibadan, NG",
    icon : ImageVector? = null,
    isMainScreen : Boolean = true,
    elevation : Dp = 0.dp,
    favViewModel: FavViewModel = hiltViewModel(),
    navController: NavController,
    onActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val showDialog = remember{mutableStateOf(false)}

    val showIt = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    if (showDialog.value){
        ShowSettingDropDownMenu(showDialog , navController)
    }

    CenterAlignedTopAppBar(
        title = { Text(
            text = appBarTitle,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
//            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
        )},
        actions = {
            if (isMainScreen){

                var showAppIntro2 by remember {
                    mutableStateOf(true)
                }

                IntroShowcase(
                    showIntroShowCase = showAppIntro2,
                    dismissOnClickOutside = true,
                    onShowCaseCompleted = { showAppIntro2 = false }
                ) {
                    IconButton(

                        modifier = Modifier.introShowCaseTarget(
                            index = 1,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = Color(0xFF7C99AC), // specify color of background
                                backgroundAlpha = 0.5f, // specify transparency of background
                                targetCircleColor = Color.White // specify color of target circle
                            ),
                        ){
                            Column {
                                Text(
                                    text = "Search your favourite cities!!",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "You can search your favourite cities by clicking here.",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Icon(
                                    painterResource(id = R.drawable.search_example),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .align(Alignment.End),
                                    tint = Color.White
                                )
                            }
                        },

                        onClick = { onActionClicked.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon"
                        )
                    }

                }

                IconButton(
                    onClick = {
                        showDialog.value = true
                    }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More"
                    )
                }
            }
        },
        navigationIcon = {
//            if (icon != null){
//                IconButton(onClick = { }) {
//                    Icon(
//                        imageVector = icon,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.secondary,
//                    )
//                }
//            }
            if (isMainScreen){
                 val isAlreadyFavList = favViewModel
                     .favList.collectAsState().value.filter { item ->
                         (item.city == appBarTitle.split(",")[0])
                     }
                 if (isAlreadyFavList.isEmpty()){
                     var showAppIntro by remember {
                         mutableStateOf(true)
                     }
                     IntroShowcase(
                         showIntroShowCase = showAppIntro,
                         dismissOnClickOutside = true,
                         onShowCaseCompleted = { showAppIntro = false }
                     ) {
                         IconButton(

                             // Intro
                             modifier = Modifier.introShowCaseTarget(
                                 index = 0,
                                 style = ShowcaseStyle.Default.copy(
                                     backgroundColor = Color(0xFF9AD0EC),
                                     backgroundAlpha = 0.98f,
                                     targetCircleColor = Color(0xFFFFC400)
                                 )
                             ){
                              Column {
                                  Text(
                                      text = "Add to Favourites!!",
                                      color = Color.White,
                                      fontSize = 24.sp,
                                      fontWeight = FontWeight.Bold
                                  )

                                  Text(
                                      text = "You can add your favourite cities to favourites by clicking here.",
                                      color = Color.White,
                                      fontSize = 16.sp
                                  )
                              }
                             },

                             onClick = {
                                 favViewModel.addToFav(Favourite(appBarTitle.split(",")[0], appBarTitle.split(",")[1]))
                                     .run {
                                         showIt.value = true
                                     }
                             }) {
                             Icon(
                                 imageVector = Icons.Filled.Favorite,
                                 contentDescription = null,
                                 tint = Color.Red.copy(alpha = 0.6f),
                             )
                         }
                     }

                 }else { showIt.value = false
                     Box {} }
                ShowToast(context = context, showIt)
             }
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun ShowToast(context: Context, showIt: MutableState<Boolean>) {
    if (showIt.value) {
        Toast.makeText(context, " Added to Favorites",
            Toast.LENGTH_LONG).show()
    }
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, navController: NavController) {
    var expanded by remember { mutableStateOf(true) }
    val items = listOf("About", "Favorites", "Settings")
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopEnd)
        .absolutePadding(top = 45.dp, right = 20.dp)) {
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false},
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)) {
            items.forEachIndexed { index, text ->
                DropdownMenuItem(
                    onClick = { expanded = false
                        showDialog.value = false},
                    text = {
                        Text(text = text,
                        modifier = Modifier.clickable {
                            navController.navigate(
                                when (text) {
                                    "About" -> Screens.AboutScreen.name
                                    "Favorites" -> Screens.FavouriteScreen.name
                                    else -> Screens.SettingsScreen.name
                                })
                        },
                        fontWeight = FontWeight.W300)
                           },
                    leadingIcon = {
                        Icon(
                            imageVector = when (text) {
                                "About" -> Icons.Default.Info
                                "Favorites" -> Icons.Default.FavoriteBorder
                                else -> {
                                    Icons.Default.Settings
                                }
                            }, contentDescription = null,
                            tint = Color.LightGray
                        )
                    },
                )
            }
        }
    }
}

