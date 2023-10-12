package com.mtech.sj.bff.util
import kotlinx.serialization.Serializable

@Serializable
data class JwtPayloadDto(
        val sub: String,
        val iss: String,
        val clientId: String,
        val originJti: String,
        val eventId: String,
        val tokenUse: String,
        val scope: String,
        val authTime: String,
        val exp: String,
        val iat: String,
        val jti: String,
        val username: String
)