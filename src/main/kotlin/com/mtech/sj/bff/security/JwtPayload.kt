package com.mtech.sj.bff.security

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mtech.sj.bff.exception.UnauthorizedException
import java.util.*

data class JwtPayload(
    @JsonProperty("cognito:username")
    val userId: String,
    val email: String
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "user-id" to userId,
            "email" to email
        )
    }

    companion object {
        fun parseIdToken(idToken: String, objectMapper: ObjectMapper): JwtPayload {
            val splitToken = idToken.split('.')
            if (splitToken.size != 3) {
                throw UnauthorizedException("Invalid JWT token")
            }
            val tokenBody = splitToken[1]

            val decodedJson = String(Base64.getUrlDecoder().decode(tokenBody))

            return objectMapper.readValue(decodedJson)
        }
    }
}
