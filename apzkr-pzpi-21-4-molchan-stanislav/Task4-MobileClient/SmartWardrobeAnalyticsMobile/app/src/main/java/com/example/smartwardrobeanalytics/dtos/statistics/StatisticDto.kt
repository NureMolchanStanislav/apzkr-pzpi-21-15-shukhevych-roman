package com.example.smartwardrobeanalytics.dtos.statistics

data class StatisticComboDto(
    val userId: String,
    val combination: List<String>,
    val usageCount: Int,
    val date: String
)