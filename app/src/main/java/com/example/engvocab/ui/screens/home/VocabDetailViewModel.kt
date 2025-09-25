package com.example.engvocab.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.engvocab.data.model.VocabDetailUiState
import com.example.engvocab.data.repository.VocabRepository
import kotlinx.coroutines.launch

class VocabDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: VocabRepository = VocabRepository()
) : ViewModel() {

    var uiState by mutableStateOf(VocabDetailUiState(isLoading = true))
        private set

    private val vocabId: String = checkNotNull(savedStateHandle["vocabId"])

    init {
        loadVocabularyDetail(vocabId)
    }

    private fun loadVocabularyDetail(id: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val vocab = repository.getVocabularyById(id)
                uiState = if (vocab != null) {
                    uiState.copy(vocabulary = vocab, isLoading = false)
                } else {
                    uiState.copy(error = "Không tìm thấy từ vựng", isLoading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = "Lỗi tải chi tiết: ${e.message}", isLoading = false)
            }
        }
    }
}