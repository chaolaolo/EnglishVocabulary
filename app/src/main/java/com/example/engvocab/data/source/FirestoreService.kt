package com.example.engvocab.data.source

import com.example.engvocab.data.model.Topics
import com.example.engvocab.data.model.Vocabulary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val wordsCollection = db.collection("OxfordWords")
    private val topicsCollection = db.collection("Topics")

    suspend fun getAllVocabulary(): List<Vocabulary> {
        return try {
            val snapshot = wordsCollection.get().await()
//            snapshot.toObjects(Vocabulary::class.java)
            snapshot.documents.map { document ->
                document.toObject(Vocabulary::class.java)?.copy(id = document.id) ?: Vocabulary()
            }
        } catch (e: Exception) {
            println("Error fetching vocabulary: ${e.message}")
            emptyList()
        }
    }

    suspend fun getVocabularyPage(
        pageSize: Long,
        lastWordInPreviousPage: String? = null
    ): List<Vocabulary> {
        return try {
            // 1. Tạo truy vấn cơ bản: Sắp xếp theo từ (word)
            var query: Query = wordsCollection
                .orderBy("word", Query.Direction.ASCENDING)
                .limit(pageSize)

            // 2. Xử lý phân trang: Bắt đầu từ từ cuối cùng của trang trước
            if (lastWordInPreviousPage != null) {
                query = query.startAfter(lastWordInPreviousPage)
            }

            val snapshot = query.get().await()

            // 3. Map kết quả sang đối tượng Vocabulary và thêm ID
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