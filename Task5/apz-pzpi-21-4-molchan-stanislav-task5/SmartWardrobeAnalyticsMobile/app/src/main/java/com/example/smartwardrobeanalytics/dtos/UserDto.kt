package com.example.smartwardrobeanalytics.dtos

data class User(
    val id: String,
    val guestId: String?,
    val roles: List<RoleDto>,
    val phone: String?,
    val email: String?,
    val password: String?,
    val isDeleted: Boolean
)