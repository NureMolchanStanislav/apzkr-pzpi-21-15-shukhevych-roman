package com.example.smartwardrobeanalytics.dtos

data class User(
    val id: String,
    val guestId: String?,
    val roles: List<RoleDto>,
    val phone: String?,
    val email: String?,
    val password: String?,
    var isDeleted: Boolean
)

data class UserListResponse(
    val items: List<User>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
)