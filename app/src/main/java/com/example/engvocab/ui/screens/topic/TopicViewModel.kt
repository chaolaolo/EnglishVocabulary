package com.example.engvocab.ui.screens.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.engvocab.data.model.SubTopicUiState
import com.example.engvocab.data.model.TopicUiState
import com.example.engvocab.data.repository.TopicsRepository
import kotlinx.coroutines.launch

class TopicViewModel(private val repository: TopicsRepository) : ViewModel() {

    var uiState: TopicUiState by mutableStateOf(TopicUiState.Loading)
        private set

    var subTopicUiState: SubTopicUiState by mutableStateOf(SubTopicUiState.Empty)
        private set

    init {
        loadTopics()
    }

    private fun loadTopics() {
        viewModelScope.launch {
            uiState = TopicUiState.Loading
            try {
                val topics = repository.getTopics()
                if (topics.isNotEmpty()) {
                    uiState = TopicUiState.Success(topics)
                } else {
                    uiState = TopicUiState.Error("No topics found")
                }
            } catch (e: Exception) {
                uiState = TopicUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadSubTopics(topicId: String) {
        viewModelScope.launch {
            subTopicUiState = SubTopicUiState.Loading
            try {
                val subTopics = repository.getSubTopicById(topicId)

                if (subTopics.isNotEmpty()) {
                    subTopicUiState = SubTopicUiState.Success(subTopics)
                } else {
                    subTopicUiState = SubTopicUiState.Error("No sub-topics found for this topic.")
                }
            } catch (e: Exception) {
                subTopicUiState = SubTopicUiState.Error(e.message ?: "Failed to load sub-topics")
            }
        }
    }
}