package com.example.engvocab.data.model

sealed interface TopicUiState {
    data object Loading : TopicUiState
    data class Success(val topics: List<Topics>) : TopicUiState
    data class Error(val message: String) : TopicUiState
}

sealed interface SubTopicUiState {
    data object Loading : SubTopicUiState
    data class Success(val subTopics: List<SubTopic>) : SubTopicUiState
    data class Error(val message: String) : SubTopicUiState
    data object Empty : SubTopicUiState
}