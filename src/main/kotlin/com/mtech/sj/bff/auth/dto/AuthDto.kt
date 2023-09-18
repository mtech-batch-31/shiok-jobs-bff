package com.mtech.sj.bff.auth.dto

import jakarta.validation.constraints.Email

data class RegisterRequest(
    @field:Email(message = "Username must be a valid email address.")
    val username: String,
    val password: String
)

data class LoginRequest(val username: String, val password: String)

data class RefreshTokenRequest(val refreshToken: String)

data class TokenResponse(val idToken: String, val accessToken: String, val refreshToken: String?)
