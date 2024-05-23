package com.example.smartwardrobeanalytics.dtos.updateDto

data class NotificationUpdateDto(
    val id: String,
    val condition: Int,
    val title: String,
    val description: String,
    val itemId: String
)