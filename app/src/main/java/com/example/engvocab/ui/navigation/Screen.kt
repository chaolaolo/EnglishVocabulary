package com.example.engvocab.ui.navigation

import com.example.engvocab.R

sealed class Screen(val route: String, val title: String, val icon: Int? = null) {
    object Home : Screen("home", "Home", R.drawable.round_home)
    object Topic : Screen("topic", "Topic", R.drawable.round_category)
    object Reading : Screen("reading", "Reading", R.drawable.round_chrome_reader_mode)
    object Search : Screen("search", "Search", R.drawable.round_search)
    object VocabDetail : Screen("vocab_detail/{vocabId}", "Detail") {
        fun createRoute(vocabId: String) = "vocab_detail/$vocabId"
    }

    object SubTopic : Screen("sub_topic/{topicId}", "Sub Topics") {
        fun createRoute(topicId: String) = "sub_topic/$topicId"
    }

    object VocabularyOfTopic : Screen("vocabulary_of_topic/{topicName}", "VocabularyOfTopic"){
        fun createRoute(topicName: String) = "vocabulary_of_topic/$topicName"
    }


    object SubReadingTopic : Screen("sub_reading_topic/{readingId}", "Sub Reading Topics") {
        fun createRoute(readingId: String) = "sub_reading_topic/$readingId"
    }
    object StoriesScreen : Screen("stories/{readingId}/{subTopicId}", "Stories") {
        fun createRoute(readingId: String, subTopicId: String) =
            "stories/$readingId/$subTopicId"
    }
    object StoryScreen : Screen("story/{readingId}/{subTopicId}/{storyId}", "Story") {
        fun createRoute(readingId: String, subTopicId: String, storyId: String) =
            "story/$readingId/$subTopicId/$storyId"
    }

}