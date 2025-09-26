package com.example.engvocab.data.repository

import com.example.engvocab.data.model.SubTopic
import com.example.engvocab.data.model.Topics
import com.example.engvocab.data.source.FirestoreService

class TopicsRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun getTopics(): List<Topics> {
        return firestoreService.getAllTopics()
    }

    suspend fun getSubTopicById(topicId: String): List<SubTopic> {
        val topic = firestoreService.getSubTopicById(topicId)

        // Trả về danh sách subTopics, hoặc danh sách rỗng nếu topic là null hoặc subTopics là null
        return topic?.subTopics ?: emptyList()
    }
}