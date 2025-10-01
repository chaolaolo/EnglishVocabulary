package com.example.engvocab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.engvocab.ui.navigation.BottomNavGraph
import com.example.engvocab.ui.navigation.Screen
import com.example.engvocab.ui.theme.EngVocabTheme
import com.example.engvocab.util.AdMobBanner
import com.example.engvocab.util.InterstitialAdManager
import com.example.engvocab.util.ThemePreference
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val showMainContent = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MobileAds.initialize(this) {}

//        InterstitialAdManager
        val adManager = InterstitialAdManager(this)
        adManager.loadAd {
            showMainContent.value = true
        }

        val themePreference = ThemePreference(applicationContext)
        setContent {

            val isDarkTheme by themePreference.isDarkTheme.collectAsState(initial = false)

            val isReady by remember { showMainContent }

            EngVocabTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false
            ) {
                if (isReady) { // isReady hiện có kiểu Boolean
                    AppContent(themePreference, isDarkTheme)
                } else {
                    LoadingSplashScreen()
                }
//                val navController = rememberNavController()
//                Scaffold(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.background),
//                    bottomBar = {
//                        val navBackStackEntry by navController.currentBackStackEntryAsState()
//                        val currentRoute = navBackStackEntry?.destination?.route
//
//                        // 👇 ẩn BottomBar khi vào detail
//                        if (currentRoute in listOf(
//                                Screen.Home.route,
//                                Screen.Topic.route,
//                                Screen.Reading.route,
//                                Screen.Search.route,
//                            )
//                        ) {
//                            Column {
//                                // 1. Bottom Menu
//                                BottomBar(navController = navController)
//
//                                // 2. Quảng cáo Banner
//                                AdMobBanner()
//                            }
//                        }
//                    }) { innerPadding ->
//                    BottomNavGraph(
//                        navController = navController,
//                        modifier = Modifier
//                            .padding(innerPadding)
//                            .background(MaterialTheme.colorScheme.background),
//                        onThemeChange = { isDark ->
//                            lifecycleScope.launch {
//                            themePreference.saveTheme(isDark)
//                        }},
//                        isDarkTheme = isDarkTheme
//                    )
//                }
            }
        }
    }

    @Composable
    fun AppContent(themePreference: ThemePreference, isDarkTheme: Boolean) {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 👇 ẩn BottomBar khi vào detail
                if (currentRoute in listOf(
                        Screen.Home.route,
                        Screen.Topic.route,
                        Screen.Reading.route,
                        Screen.Search.route,
                    )
                ) {
                    Column {
                        // 1. Bottom Menu
                        BottomBar(navController = navController)

                        // 2. Quảng cáo Banner
                        AdMobBanner()
                    }
                }
            }) { innerPadding ->
            BottomNavGraph(
                navController = navController,
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                onThemeChange = { isDark ->
                    lifecycleScope.launch {
                        themePreference.saveTheme(isDark)
                    }
                },
                isDarkTheme = isDarkTheme
            )
        }

    }

}

@Composable
fun LoadingSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer), // Màu nền có thể là màu thương hiệu của bạn
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Home,
        Screen.Topic,
        Screen.Reading,
        Screen.Search,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = {
                    screen.icon?.let {
                        Icon(
                            painterResource(id = it),
                            contentDescription = null
                        )
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    EngVocabTheme {
//        Greeting("Android")
    }
}