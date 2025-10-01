package com.example.engvocab.util

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.engvocab.data.repository.ReadingRepository
import com.example.engvocab.data.repository.TopicsRepository
import com.example.engvocab.data.source.FirestoreService
import com.example.engvocab.data.source.ReadingService
import com.example.engvocab.ui.screens.reading.ReadingViewModel
import com.example.engvocab.ui.screens.topic.TopicViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // ViewModel cho TopicScreen
        initializer {
            val firestoreService = FirestoreService()
            val repository = TopicsRepository(firestoreService)
            TopicViewModel(repository)
        }

        initializer {
            val readingService = ReadingService()
            val repository = ReadingRepository(readingService)
            ReadingViewModel(repository)
        }

    }
}