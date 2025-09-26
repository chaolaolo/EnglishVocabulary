package com.example.engvocab.ui.navigation

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.engvocab.ui.screens.home.HomeScreen
import com.example.engvocab.ui.screens.topic.TopicScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.engvocab.ui.screens.home.VocabDetail
import com.example.engvocab.ui.screens.home.VocabularyOfTopicScreen
import com.example.engvocab.ui.screens.search.SearchScreen
import com.example.engvocab.ui.screens.topic.SubTopics

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
                onThemeChange = onThemeChange
            )
        }
        composable(Screen.Topic.route) {
            TopicScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(
            Screen.VocabDetail.route,
            arguments = listOf(navArgument("vocabId") { type = NavType.StringType })
        ) {backStackEntry ->
            val vocabId = backStackEntry.arguments?.getString("vocabId")

            // 🚀 Kiểm tra và gọi VocabDetail với ID
            if (vocabId != null) {
                VocabDetail(
                    navController = navController,
                    vocabId = vocabId // 🚀 Truyền ID vào Composable
                )
            } else {
                // Xử lý lỗi nếu ID bị thiếu
                VocabDetail(
                    navController = navController,
                    vocabId = "" // Truyền rỗng hoặc xử lý lỗi
                )
            }
        }
        composable(
            Screen.SubTopic.route,
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId")
            if (topicId != null) {
                SubTopics(
                    navController = navController,
                    topicId = topicId
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(
            Screen.VocabularyOfTopic.route,
            arguments = listOf(navArgument("topicName") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTopicName = backStackEntry.arguments?.getString("topicName")

            if (encodedTopicName != null) {
                // Giải mã chỉ khi tham số gốc không null
                val topicName = java.net.URLDecoder.decode(
                    encodedTopicName,
                    java.nio.charset.StandardCharsets.UTF_8.toString()
                )

                VocabularyOfTopicScreen(
                    navController = navController,
                    topicName = topicName
                )
            } else {
                // Nếu tham số là null, in ra log lỗi và quay lại
                Log.e("NavGraph", "Missing 'topicName' argument for VocabularyOfTopicScreen")
                navController.popBackStack()
            }
        }
    }
}