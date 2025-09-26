package com.example.engvocab.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.engvocab.data.model.HomeUiState
import com.example.engvocab.data.repository.VocabRepository
import kotlinx.coroutines.launch
import kotlin.math.ceil

private const val PAGE_SIZE = 100L

class HomeViewModel(
    private val repository: VocabRepository = VocabRepository()
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState(isLoading = true))
        private set
    var topicVocabUiState by mutableStateOf(HomeUiState(isLoading = false))
        private set

    private val pageAnchors = mutableListOf<String?>()
    private var isFetchingNextPage = false

    init {
        pageAnchors.add(null) // Trang 1 bắt đầu từ null
        fetchVocabularyPage(1)
    }

    private fun fetchVocabularyPage(page: Int) {
        viewModelScope.launch {
            if (isFetchingNextPage) return@launch // Tránh gọi API liên tục

            uiState = uiState.copy(isLoading = true, error = null)
            isFetchingNextPage = true

            try {
                // Lấy điểm neo (từ cuối cùng của trang N-1)
                val lastWord = pageAnchors[page - 1]

                // Tải trang N từ Firestore
                val newPageList = repository.getVocabularyPage(PAGE_SIZE, lastWord)

                // Cập nhật trạng thái
                uiState = uiState.copy(
                    vocabularyOnPage = newPageList,
                    currentPage = page,
                    // Không còn tính totalPages chính xác, sử dụng một giá trị lớn để hiển thị nút Next
                    totalPages = pageAnchors.size + 1,
                    isLoading = false
                )

                // Lưu từ cuối cùng của trang hiện tại làm điểm neo cho trang tiếp theo
                if (pageAnchors.size == page && newPageList.isNotEmpty()) {
                    // Nếu đây là trang mới (chưa có trong anchors)
                    val nextAnchor = newPageList.last().word?.toLowerCase()
                    pageAnchors.add(nextAnchor)
                    uiState =
                        uiState.copy(totalPages = pageAnchors.size) // Cập nhật totalPages giả định
                }

            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = "Error fetching vocabulary: ${e.message}",
                    isLoading = false,
                )
            } finally {
                isFetchingNextPage = false
            }
        }
    }

    fun nextPage() {
        val next = uiState.currentPage + 1
        if (next <= uiState.totalPages) {
            // Nếu trang tiếp theo đã có điểm neo (đã từng được tải), chỉ cần hiển thị
            if (next < pageAnchors.size) {
                fetchVocabularyPage(next)
            } else if (next == pageAnchors.size) {
                // Nếu trang tiếp theo chưa được tải (là trang mới)
                fetchVocabularyPage(next)
            }
        }
    }

    fun previousPage() {
        val previous = (uiState.currentPage - 1).coerceAtLeast(1)
        if (previous != uiState.currentPage) {
            fetchVocabularyPage(previous)
        }
    }

    @SuppressLint("DefaultLocale")
    fun refreshVocabulary(isManualRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isManualRefresh) {
                uiState = uiState.copy(isRefreshing = true, error = null)
            } else {
                uiState = uiState.copy(isLoading = true, error = null)
            }

            pageAnchors.clear()
            pageAnchors.add(null)

            fetchVocabularyPage(1)

        }
    }

    fun loadVocabularyByTopic(topicName: String) {
        viewModelScope.launch {
            topicVocabUiState = topicVocabUiState.copy(isLoading = true, error = null)
            try {
                val vocabList = repository.getVocabularyByTopic(topicName)

                if (vocabList.isNotEmpty()) {
                    topicVocabUiState = topicVocabUiState.copy(
                        vocabularyOnPage = vocabList,
                        isLoading = false
                    )
                } else {
                    topicVocabUiState = topicVocabUiState.copy(
                        error = "No words found for topic: $topicName",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                topicVocabUiState = topicVocabUiState.copy(
                    error = e.message ?: "Failed to load vocabulary by topic",
                    isLoading = false
                )
            }
        }

    }
}