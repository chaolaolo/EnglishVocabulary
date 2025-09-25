package com.example.engvocab.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.engvocab.ui.screens.home.HomeScreen
import com.example.engvocab.ui.screens.topic.TopicScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.engvocab.ui.screens.home.VocabDetail
import com.example.engvocab.ui.screens.search.SearchScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val isDarkTheme = isSystemInDarkTheme()
            HomeScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange )
        }
        composable(Screen.Topic.route) {
            TopicScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.VocabDetail.route) {
            VocabDetail(navController = navController)
        }
    }
}