package com.example.engvocab.data.source

import com.example.engvocab.data.model.Reading
import com.example.engvocab.data.model.Story
import com.example.engvocab.data.model.SubReadingTopic
import com.example.engvocab.data.model.Topics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ReadingService(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    private val readingCollection = db.collection("Reading")

    //    getReadings
    suspend fun getReadings(): List<Reading> {
        return try {
            val snapshot = readingCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(Reading::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            println("Error fetching readings: ${e.message}")
            emptyList()
        }
    }

    suspend fun getReading(readingId: String): Reading {
        return try {
            val document = readingCollection.document(readingId).get().await()
            document.toObject(Reading::class.java)?.copy(id = document.id)
                ?: throw Exception("Reading not found")
        } catch (e: Exception) {
            println("Error fetching reading $readingId: ${e.message}")
            throw e
        }
    }

    //    getSubReadingTopics
    suspend fun getSubReadingTopics(readingId: String): List<SubReadingTopic> {
        return try {
            val subTopicsCollection = readingCollection.document(readingId).collection("SubTopics")
            val snapshot = subTopicsCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(SubReadingTopic::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            println("Error fetching sub-reading topics for $readingId: ${e.message}")
            emptyList()
        }
    }

    suspend fun getSubReadingTopicById(readingId: String, subTopicId: String): SubReadingTopic {
        return try {
            val document = readingCollection
                .document(readingId)
                .collection("SubTopics")
                .document(subTopicId)
                .get()
                .await()
            document.toObject(SubReadingTopic::class.java)?.copy(id = document.id)
                ?: throw Exception("SubReadingTopic not found")
        } catch (e: Exception) {
            println("Error fetching sub-reading topic $subTopicId: ${e.message}")
            throw e
        }
    }

    //    getStories
    suspend fun getStories(readingId: String, subTopicId: String): List<Story> {
        return try {
            val storiesCollection = readingCollection
                .document(readingId)
                .collection("SubTopics")
                .document(subTopicId)
                .collection("Stories")

            val snapshot = storiesCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(Story::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            println("Error fetching stories for $subTopicId: ${e.message}")
            emptyList()
        }
    }

    //    getStory
    suspend fun getStory(readingId: String, subTopicId: String, storyId: String): Story? {
        return try {
            val storyDocument = readingCollection
                .document(readingId)
                .collection("SubTopics")
                .document(subTopicId)
                .collection("Stories")
                .document(storyId)

            val document = storyDocument.get().await()
            document.toObject(Story::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            println("Error fetching story $storyId: ${e.message}")
            null
        }
    }

}