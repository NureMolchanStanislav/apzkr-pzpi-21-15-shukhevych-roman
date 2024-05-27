package com.example.smartwardrobeanalytics.dtos.statistics

data class SeasonalStatisticDto(
    val season: String,
    val totalUsages: Int,
    val itemUsages: Map<String, Int>
)
