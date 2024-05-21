package com.example.smartwardrobeanalytics.dtos

data class StatisticDto(
    val month: String,
    val usageCount: Int
)

data class UsageDto(
    val name: String,
    val date: String
)