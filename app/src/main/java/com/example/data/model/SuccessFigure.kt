package com.example.data.model

data class SuccessFigure(
    val id: String,
    val name: String,
    val era: String,
    val category: String,
    val shortBio: String,
    val principleId: Int, // 1 to 13 (or 0)
    val principleName: String,
    val secondaryPrinciple: String? = null,
    val exemplaryMoment: String, // 2-4 sentences: specific factual moment/decision/pattern
    val quote: String? = null,
    val quoteSource: String? = null,
    val keyTakeaway: String,
    val tags: List<String> = emptyList()
)
