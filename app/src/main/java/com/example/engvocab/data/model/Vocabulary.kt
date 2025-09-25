//package com.example.engvocab.data.model
//
//data class Vocabulary(
//    val word: String? = null,
//    // Các trường chi tiết về từ
//    val detailUrl: String? = null,
//    val level: String? = null,
//    val partOfSpeech: String? = null,
//
//    // Phần chứa phát âm
//    val phonetics: Phonetics? = null,
//
//    // Danh sách các nghĩa (senses) của từ
//    val senses: List<Sense>? = null,
//
//    // Các trường khác như "type", "verbForms" có thể được thêm vào đây
//    val type: String? = null, // Ví dụ: "indefinite article"
//    val verbForms: VerbForms? = null
//)
//
//// --- Lớp cho Phát âm (Phonetics) ---
//data class Phonetics(
//    val uk: PronunciationDetail? = null,
//    val us: PronunciationDetail? = null
//)
//
//// Chi tiết phát âm (cho UK và US)
//data class PronunciationDetail(
//    val audio: String? = null,
//    val text: String? = null
//)
//
//// --- Lớp cho Nghĩa (Sense) ---
//data class Sense(
//    // Mặc định, nghĩa đầu tiên thường không có số thứ tự rõ ràng,
//    // nhưng các nghĩa tiếp theo được đánh số (0, 1, 2,...)
//    val definition: String? = null,
//    // Danh sách các ví dụ minh họa cho nghĩa này
//    val examples: List<String>? = null,
//    // Trường 'senses' lồng bên trong một Sense khác (thường gặp trong từ điển)
//    // Ví dụ: Sense 0 có các sub-sense 0, 1, 2...
//    val senses: List<Sense>? = null
//)
//
//// --- Lớp cho Dạng Động từ (VerbForms) ---
//// Dựa trên ảnh, trường này chỉ chứa một trường "word"
//data class VerbForms(
//    val word: String? = null // Ví dụ: "a"
//)


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
    val verbForms: List<VerbForms>? = null
)

// --- Lớp cho Phát âm (Phonetics) ---
data class Phonetics(
    val uk: PronunciationDetail? = null,
    val us: PronunciationDetail? = null
)

// Chi tiết phát âm (cho UK và US)
data class PronunciationDetail(
    val audio: String? = null,
    val text: String? = null
)

// --- Lớp cho Nghĩa (Sense) ---
data class Sense(
    val definition: String? = null,
    val examples: List<String>? = null,
    val senses: List<Sense>? = null
)

// --- Lớp cho Dạng Động từ (VerbForms) ---
data class VerbForms(
    val word: String? = null // Ví dụ: "a"
)