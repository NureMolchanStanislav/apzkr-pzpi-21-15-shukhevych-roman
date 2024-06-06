package com.example.smartwardrobeanalytics.dtos

data class BrandDto(
    val id: String,
    val name: String
)

data class BrandResponse(
    val items: List<BrandDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)