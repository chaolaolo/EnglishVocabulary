package com.example.engvocab.data.model

// Topics
data class Topics(
    val id: String? = null,
    val title: String? = null,
    val detailUrl: String? = null,
    val imageUrl: String? = null,
    val subTopics: List<SubTopic>? = null,
)


data class SubTopic(
    val name: String? = null,
    val url: String? = null
)