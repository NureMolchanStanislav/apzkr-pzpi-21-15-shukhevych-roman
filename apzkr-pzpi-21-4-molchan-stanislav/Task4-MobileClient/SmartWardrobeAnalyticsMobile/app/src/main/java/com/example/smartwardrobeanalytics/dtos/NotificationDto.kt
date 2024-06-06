package com.example.smartwardrobeanalytics.dtos

data class NotificationDto(
    val id: String,
    val condition: Int,
    val title: String,
    val description: String,
    val itemId: String
)