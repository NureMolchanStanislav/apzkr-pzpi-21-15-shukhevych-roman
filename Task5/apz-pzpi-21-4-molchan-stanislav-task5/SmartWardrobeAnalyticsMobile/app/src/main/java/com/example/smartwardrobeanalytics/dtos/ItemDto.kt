package com.example.smartwardrobeanalytics.dtos

data class ItemDto(
    val id: String,
    val name: String,
    val description: String,
    val categories: Int,
    val brandId: String,
    val collectionId: String
)