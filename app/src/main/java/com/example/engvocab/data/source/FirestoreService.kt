package com.example.engvocab.data.source

import com.example.engvocab.data.model.Vocabulary
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val wordsCollection = db.collection("OxfordWords")

    suspend fun getAllVocabulary(): List<Vocabulary> {
        return try {
            val snapshot = wordsCollection.get().await()
            snapshot.toObjects(Vocabulary::class.java)
        } catch (e: Exception) {
            println("Error fetching vocabulary: ${e.message}")
            emptyList()
        }
    }
}