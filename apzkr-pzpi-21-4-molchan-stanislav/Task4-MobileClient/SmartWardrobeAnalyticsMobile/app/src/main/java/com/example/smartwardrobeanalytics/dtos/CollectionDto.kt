package com.example.smartwardrobeanalytics.dtos

data class CollectionDto(
    val id: String,
    val name: String,
    val description: String
)

data class CollectionResponse(
    val items: List<CollectionDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)