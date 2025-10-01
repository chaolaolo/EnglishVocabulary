package com.example.engvocab.data.model

// Trạng thái cho màn hình hiển thị danh sách Readings (ví dụ: ReadingScreen)
data class ReadingsUiState(
    val readings: List<Reading> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Trạng thái cho màn hình SubTopics
data class SubTopicsUiState(
    val subTopics: List<SubReadingTopic> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Trạng thái cho màn hình Stories
data class StoriesUiState(
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Trạng thái cho màn hình chi tiết Story
data class StoryDetailUiState(
    val story: Story? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)