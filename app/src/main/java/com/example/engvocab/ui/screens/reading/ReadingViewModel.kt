package com.example.engvocab.ui.screens.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.engvocab.data.model.Reading
import com.example.engvocab.data.model.ReadingsUiState
import com.example.engvocab.data.model.StoriesUiState
import com.example.engvocab.data.model.StoryDetailUiState
import com.example.engvocab.data.model.SubTopicsUiState
import com.example.engvocab.data.repository.ReadingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadingViewModel(
    private val repository: ReadingRepository
) : ViewModel() {
    // Trạng thái cho màn hình Readings (ví dụ: ReadingScreen)
    private val _readingsUiState = MutableStateFlow(ReadingsUiState())
    val readingsUiState: StateFlow<ReadingsUiState> = _readingsUiState

    // Trạng thái cho màn hình SubTopics
    private val _subTopicsUiState = MutableStateFlow(SubTopicsUiState())
    val subTopicsUiState: StateFlow<SubTopicsUiState> = _subTopicsUiState

    // Trạng thái cho màn hình Stories
    private val _storiesUiState = MutableStateFlow(StoriesUiState())
    val storiesUiState: StateFlow<StoriesUiState> = _storiesUiState

    // Trạng thái cho màn hình chi tiết Story
    private val _storyDetailUiState = MutableStateFlow(StoryDetailUiState())
    val storyDetailUiState: StateFlow<StoryDetailUiState> = _storyDetailUiState

    private val _readingTitle = MutableStateFlow<String?>(null)
    val readingTitle: StateFlow<String?> = _readingTitle

    private val _subReadingTopicTitle = MutableStateFlow<String?>(null)
    val subReadingTopicTitle: StateFlow<String?> = _subReadingTopicTitle


    init {
        // Tải danh sách Readings ngay khi ViewModel được tạo
        fetchReadings()
    }

    fun fetchReadings() {
        _readingsUiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val readings = repository.getReadings()
                _readingsUiState.update {
                    it.copy(
                        readings = readings,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _readingsUiState.update {
                    it.copy(
                        error = "Lỗi khi tải Readings: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchReadingById(readingId: String) {
        viewModelScope.launch {
            try {
                val reading = repository.getReading(readingId)
                _readingTitle.value = reading?.name
            } catch (e: Exception) {
                _readingTitle.value = null
            }
        }
    }

    fun fetchSubReadingTopics(readingId: String) {
        _subTopicsUiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val subTopics = repository.getSubReadingTopics(readingId)
                _subTopicsUiState.update { it.copy(subTopics = subTopics, isLoading = false) }
            } catch (e: Exception) {
                _subTopicsUiState.update {
                    it.copy(
                        error = "Lỗi khi tải SubTopics: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchSubReadingTopicById(readingId: String, subTopicId: String) {
        viewModelScope.launch {
            try {
                val subTopic = repository.getSubReadingTopicById(readingId, subTopicId)
                _subReadingTopicTitle.value = subTopic?.title
            } catch (e: Exception) {
                _subReadingTopicTitle.value = null
            }
        }
    }

    fun fetchStories(readingId: String, subTopicId: String) {
        _storiesUiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val stories = repository.getStories(readingId, subTopicId)
                _storiesUiState.update { it.copy(stories = stories, isLoading = false) }
            } catch (e: Exception) {
                _storiesUiState.update {
                    it.copy(
                        error = "Lỗi khi tải Stories: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchStoryDetail(readingId: String, subTopicId: String, storyId: String) {
        _storyDetailUiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val story = repository.getStory(readingId, subTopicId, storyId)
                _storyDetailUiState.update { it.copy(story = story, isLoading = false) }
            } catch (e: Exception) {
                _storyDetailUiState.update {
                    it.copy(
                        error = "Lỗi khi tải chi tiết Story: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }
}