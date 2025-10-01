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
import com.example.engvocab.ui.screens.reading.ReadingScreen
import com.example.engvocab.ui.screens.reading.StoriesScreen
import com.example.engvocab.ui.screens.reading.StoryScreen
import com.example.engvocab.ui.screens.reading.SubReadingScreen
import com.example.engvocab.ui.screens.search.SearchScreen
import com.example.engvocab.ui.screens.topic.SubTopics

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onThemeChange: (Boolean) -> Unit,
    isDarkTheme: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
        composable(Screen.Topic.route) {
            TopicScreen(navController = navController)
        }
        composable(Screen.Reading.route) {
            ReadingScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(
            Screen.VocabDetail.route,
            arguments = listOf(navArgument("vocabId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vocabId = backStackEntry.arguments?.getString("vocabId")

            if (vocabId != null) {
                VocabDetail(
                    navController = navController,
                    vocabId = vocabId
                )
            } else {
                VocabDetail(
                    navController = navController,
                    vocabId = ""
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
                val topicName = java.net.URLDecoder.decode(
                    encodedTopicName,
                    java.nio.charset.StandardCharsets.UTF_8.toString()
                )

                VocabularyOfTopicScreen(
                    navController = navController,
                    topicName = topicName
                )
            } else {
                Log.e("NavGraph", "Missing 'topicName' argument for VocabularyOfTopicScreen")
                navController.popBackStack()
            }
        }


        composable(
            Screen.SubReadingTopic.route,
            arguments = listOf(navArgument("readingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val readingId = backStackEntry.arguments?.getString("readingId")
            if (readingId != null) {
                SubReadingScreen(
                    navController = navController,
                    readingId = readingId
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(
            route = Screen.StoriesScreen.route,
            arguments = listOf(
                navArgument("readingId") { type = NavType.StringType },
                navArgument("subTopicId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val readingId = backStackEntry.arguments?.getString("readingId")
            val subTopicId = backStackEntry.arguments?.getString("subTopicId")

            if (readingId != null && subTopicId != null) {
                StoriesScreen(
                    navController = navController,
                    readingId = readingId,
                    subTopicId = subTopicId
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(
            route = Screen.StoryScreen.route,
            arguments = listOf(
                navArgument("readingId") { type = NavType.StringType },
                navArgument("subTopicId") { type = NavType.StringType },
                navArgument("storyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val readingId = backStackEntry.arguments?.getString("readingId")
            val subTopicId = backStackEntry.arguments?.getString("subTopicId")
            val storyId = backStackEntry.arguments?.getString("storyId")

            if (readingId != null && subTopicId != null && storyId != null) {
                StoryScreen(
                    navController = navController,
                    readingId = readingId,
                    subTopicId = subTopicId,
                    storyId = storyId
                )
            } else {
                navController.popBackStack()
            }
        }

    }
}