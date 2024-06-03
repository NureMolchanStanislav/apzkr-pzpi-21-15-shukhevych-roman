package com.example.smartwardrobeanalytics.dtos

data class LoginUserDto(
    val email: String,
    val password: String
)

data class TokensModel(
    val accessToken: String,
    val refreshToken: String
)