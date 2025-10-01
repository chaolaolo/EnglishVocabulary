package com.example.engvocab.data.model

import com.google.firebase.Timestamp

// Reading
data class Reading(
    val id: String? = null,
    val createdAt: Timestamp? = null,
    val name: String? = null,
    val url: String? = null
)

// SubTopic
data class SubReadingTopic(
    val id: String? = null,
    val createdAt: Timestamp? = null,
    val title: String? = null,
    val url: String? = null,
    val image: String? = null,
    val description: String? = null
)

// Story
data class Story(
    val id: String? = null,
    val createdAt: Timestamp? = null,
    val title: String? = null,
    val image: String? = null,
    val description: String? = null,
    val url: String? = null,
    val details: StoryDetails? = null
)

// StoryDetails
data class StoryDetails(
    val audioUrl: String? = null,
    val imageUrl: String? = null,
    val headerTitle: String? = null,
    val url: String? = null,
    val content: String? = null
)

