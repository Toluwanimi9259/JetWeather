package com.techafresh.jetweather.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techafresh.jetweather.screens.about.AboutScreen
import com.techafresh.jetweather.screens.fav.FavouriteScreen
import com.techafresh.jetweather.screens.main.MainScreen
import com.techafresh.jetweather.screens.search.SearchScreen
import com.techafresh.jetweather.screens.settings.SettingsScreen
import com.techafresh.jetweather.screens.splash.SplashScreen
import com.techafresh.jetweather.viewmodel.FavViewModel
import com.techafresh.jetweather.viewmodel.WeatherViewModel

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.name
        ){

        composable(Screens.SplashScreen.name){
            SplashScreen(
                navController = navController
            )
        }

        val route = Screens.MainScreen.name
        composable("$route/{city}", arguments = listOf(navArgument(name = "city"){
           type = NavType.StringType
        })){navBack ->
            navBack.arguments?.getString("city").let {city ->
                val weatherViewModel = hiltViewModel<WeatherViewModel>()
                MainScreen(
                    navController = navController,
                    weatherViewModel,
                    city = city
                )
            }
        }

        composable(Screens.SearchScreen.name){
            SearchScreen(
                navController = navController
            )
        }

        composable(Screens.FavouriteScreen.name){
            FavouriteScreen(
                navController = navController
            )
        }

        composable(Screens.AboutScreen.name){
            AboutScreen(
                navController = navController
            )
        }

        composable(Screens.SettingsScreen.name){
            SettingsScreen(
                navController = navController
            )
        }
    }
}