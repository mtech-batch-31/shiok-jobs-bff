package com.mtech.sj.bff.security

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class JwtPayload(
    @JsonProperty("cognito:username")
    val userId: String,
    val email: String,
){
    fun toMap(): Map<String, String> {
        return mapOf(
            "user-id" to userId,
            "email" to email
        )
    }
}