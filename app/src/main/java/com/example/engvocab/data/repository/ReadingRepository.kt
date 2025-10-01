package com.example.engvocab.data.repository

import com.example.engvocab.data.model.Reading
import com.example.engvocab.data.model.Story
import com.example.engvocab.data.model.SubReadingTopic
import com.example.engvocab.data.source.ReadingService

class ReadingRepository(
    private val readingService: ReadingService
) {
    suspend fun getReadings(): List<Reading> {
        return readingService.getReadings()
    }

    suspend fun getReading(readingId: String): Reading {
        return readingService.getReading(readingId)
    }

    suspend fun getSubReadingTopics(readingId: String): List<SubReadingTopic> {
        return readingService.getSubReadingTopics(readingId)
    }
    suspend fun getSubReadingTopicById(readingId: String, subTopicId: String): SubReadingTopic {
        return readingService.getSubReadingTopicById(readingId, subTopicId)
    }

    suspend fun getStories(readingId: String, subTopicId: String): List<Story> {
        return readingService.getStories(readingId, subTopicId)
    }

    suspend fun getStory(readingId: String, subTopicId: String, storyId: String): Story? {
        return readingService.getStory(readingId, subTopicId, storyId)
    }
}