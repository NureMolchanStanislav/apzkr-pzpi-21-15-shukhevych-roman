package com.example.smartwardrobeanalytics.dtos.сreateDto

data class NotificationCreateDto(
    val condition: Int,
    val title: String,
    val description: String,
    val itemId: String
)