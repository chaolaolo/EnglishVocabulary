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

private const val PAGE_SIZE = 100

class HomeViewModel(
    private val repository: VocabRepository = VocabRepository()
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState(isLoading = true))
        private set

    init {
        fetchVocabulary()
    }

    @SuppressLint("DefaultLocale")
    private fun fetchVocabulary() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                var result = repository.getVocabularyList()
                result = result.sortedBy { it.word?.toLowerCase() }

                val totalPages = ceil(result.size.toDouble() / PAGE_SIZE).toInt().coerceAtLeast(1)

                uiState = uiState.copy(
                    vocabulary = result,
                    totalPages = totalPages,
                    isLoading = false
                )
                updateVocabularyForPage(1)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = "Error fetching vocabulary: ${e.message}",
                    isLoading = false,
                )
            }
        }
    }

    private fun updateVocabularyForPage(page: Int) {
        val startIndex = (page - 1) * PAGE_SIZE
        val endIndex = (startIndex + PAGE_SIZE).coerceAtMost(uiState.vocabulary.size)

        val listOnPage = if (startIndex < uiState.vocabulary.size) {
            uiState.vocabulary.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        uiState = uiState.copy(
            vocabularyOnPage = listOnPage,
            currentPage = page
        )
    }

    fun nextPage() {
        val next = (uiState.currentPage + 1).coerceAtMost(uiState.totalPages)
        if (next != uiState.currentPage) {
            updateVocabularyForPage(next)
        }
    }

    fun previousPage() {
        val previous = (uiState.currentPage - 1).coerceAtLeast(1)
        if (previous != uiState.currentPage) {
            updateVocabularyForPage(previous)
        }
    }

    @SuppressLint("DefaultLocale")
    fun refreshVocabulary(isManualRefresh: Boolean = false){
        viewModelScope.launch {
            if(isManualRefresh){
                uiState = uiState.copy(isRefreshing = true, error = null)
            }else{
                uiState = uiState.copy(isLoading = true, error = null)
            }

            try{
                var result = repository.getVocabularyList()
                result = result.sortedBy { it.word?.toLowerCase() }

                val totalPages = ceil(result.size.toDouble() / PAGE_SIZE).toInt().coerceAtLeast(1)

                uiState = uiState.copy(
                    vocabulary = result,
                    totalPages = totalPages,
                    isLoading = false,
                    isRefreshing = false // 🚀 TẮT TRẠNG THÁI LÀM MỚI
                )
                updateVocabularyForPage(1)
            }catch (e: Exception){
                uiState = uiState.copy(
                    error = "Error fetching vocabulary: ${e.message}",
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }
}