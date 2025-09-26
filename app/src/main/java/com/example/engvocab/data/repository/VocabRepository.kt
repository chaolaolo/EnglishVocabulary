package com.example.engvocab.data.repository

import com.example.engvocab.data.model.Vocabulary
import com.example.engvocab.data.source.FirestoreService

class VocabRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {

    suspend fun getVocabularyPage(
        pageSize: Long,
        lastWordInPreviousPage: String? = null
    ): List<Vocabulary> {
        return firestoreService.getVocabularyPage(pageSize, lastWordInPreviousPage)
    }

    suspend fun getVocabularyById(id: String): Vocabulary? {
        return firestoreService.getVocabularyById(id)
    }

    suspend fun searchVocabulary(prefix: String, limit: Long): List<Vocabulary> {
        return firestoreService.searchVocabulary(prefix, limit)
    }

    suspend fun getVocabularyByTopic(topicName: String): List<Vocabulary> {
        return firestoreService.getVocabularyByTopic(topicName)
    }
}