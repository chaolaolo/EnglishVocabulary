
package com.example.engvocab.data.model

data class Vocabulary(
    val id: String? = null,
    val word: String? = null,
    val detailUrl: String? = null,
    val level: String? = null,
    val partOfSpeech: String? = null,
    val phonetics: Phonetics? = null,
    val senses: List<Sense>? = null,
    val type: String? = null,
//    val verbForms: VerbForms? = null
    val verbForms: List<VerbForms>? = null,
    val topics: List<Topic>? = null
)

// --- Phonetics ---
data class Phonetics(
    val uk: PronunciationDetail? = null,
    val us: PronunciationDetail? = null
)

// Chi tiết Phonetics
data class PronunciationDetail(
    val audio: String? = null,
    val text: String? = null
)

// --- Sense  ---
data class Sense(
    val definition: String? = null,
    val examples: List<String>? = null,
    val senses: List<Sense>? = null
)

// --- VerbForms ---
data class VerbForms(
    val word: String? = null
)

// Topics
data class Topic(
    val name: String? = null,
    val cefr: String? = null
)