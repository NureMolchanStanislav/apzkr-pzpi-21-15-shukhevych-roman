package com.example.smartwardrobeanalytics.dtos.updateDto

data class ItemUpdateDto(
    val id: String,
    val name: String,
    val description: String,
    val brandId: String,
    val collectionId: String,
    val categories: Int = 1
)
