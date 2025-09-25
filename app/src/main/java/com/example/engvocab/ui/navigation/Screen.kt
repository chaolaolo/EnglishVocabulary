package com.example.engvocab.ui.navigation

import com.example.engvocab.R

sealed class Screen(val route: String, val title:String, val icon:Int?=null ) {
    object Home: Screen("home", "Home", R.drawable.round_home)
    object Topic: Screen("topic", "Topic", R.drawable.round_category)
    object Search: Screen("search", "Search", R.drawable.round_search)
    object VocabDetail : Screen("vocab_detail/{vocabId}","Detail"){
        fun createRoute(vocabId: String) = "vocab_detail/$vocabId"
    }
}