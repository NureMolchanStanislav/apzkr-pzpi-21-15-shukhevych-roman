package com.example.smartwardrobeanalytics.dtos.сreateDto

data class ItemCreateDto(
    val name: String,
    val description: String,
    val brandId: String,
    val collectionId: String,
    val categories: Int = 1
)