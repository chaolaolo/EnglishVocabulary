package com.example.engvocab.data.repository

import com.example.engvocab.data.model.Vocabulary
import com.example.engvocab.data.source.FirestoreService

class VocabRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {

    suspend fun getVocabularyList(): List<Vocabulary> {
        return firestoreService.getAllVocabulary()
    }
}