package com.example.engvocab.data.model

data class VocabDetailUiState(
    val vocabulary: Vocabulary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)