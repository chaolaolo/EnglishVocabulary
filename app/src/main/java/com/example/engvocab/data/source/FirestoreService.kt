package com.example.engvocab.data.source

import androidx.room.util.copy
import com.example.engvocab.data.model.Topics
import com.example.engvocab.data.model.Vocabulary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val wordsCollection = db.collection("OxfordWords")
    private val topicsCollection = db.collection("Topics")

    suspend fun getVocabularyPage(
        pageSize: Long,
        lastWordInPreviousPage: String? = null
    ): List<Vocabulary> {
        return try {
            var query: Query = wordsCollection
                .orderBy("word", Query.Direction.ASCENDING)
                .limit(pageSize)

            if (lastWordInPreviousPage != null) {
                query = query.startAfter(lastWordInPreviousPage)
            }

            val snapshot = query.get().await()

            snapshot.documents.map { document ->
                document.toObject(Vocabulary::class.java)?.copy(id = document.id) ?: Vocabulary()
            }
        } catch (e: Exception) {
            println("Error fetching vocabulary page: ${e.message}")
            emptyList()
        }
    }

    suspend fun getVocabularyById(id: String): Vocabulary? {
        return try {
            val document = wordsCollection.document(id).get().await()
            document.toObject(Vocabulary::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            println("Error fetching vocabulary: ${e.message}")
            null
        }
    }

    suspend fun searchVocabulary(prefix: String, limit: Long): List<Vocabulary> {
        return try {
            val endPrefix = prefix + "\uf8ff"
            val query = wordsCollection
                .orderBy("word", Query.Direction.ASCENDING)
                .startAt(prefix)
                .endAt(endPrefix)
                .limit(limit)

            val snapshot = query.get().await()

            snapshot.documents.map { document ->
                document.toObject(Vocabulary::class.java)?.copy(id = document.id) ?: Vocabulary()
            }
        } catch (e: Exception) {
            println("Error searching vocabulary: ${e.message}")
            emptyList()
        }
    }

    suspend fun getVocabularyByTopic(topicName: String): List<Vocabulary> {
        return try {
            val snapshot = wordsCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                val vocab = document.toObject(Vocabulary::class.java)?.copy(id = document.id)
                // Lọc những từ có ít nhất 1 topic.name khớp với topicName truyền vào
                if (vocab?.topics?.any { it.name == topicName } == true) vocab else null
            }.sortedBy { it.word }
        } catch (e: Exception) {
            println("Error fetching vocabulary for topic '$topicName': ${e.message}")
            emptyList()
        }
    }


    // Topics
    suspend fun getAllTopics(): List<Topics> {
        return try {
            val snapshot = topicsCollection
                .orderBy("title", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.map { document ->
                document.toObject(Topics::class.java)?.copy(id = document.id) ?: Topics()
            }
        } catch (e: Exception) {
            println("Error fetching topics: ${e.message}")
            emptyList()
        }
    }

    suspend fun getSubTopicById(id: String): Topics? {
        return try {
            val document = topicsCollection.document(id).get().await()

            document.toObject(Topics::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            println("Error fetching topic by ID $id: ${e.message}")
            null
        }
    }

}