package com.example.engvocab.data.model

data class HomeUiState(
    val vocabulary: List<Vocabulary> = emptyList(),
    val vocabularyOnPage: List<Vocabulary> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)