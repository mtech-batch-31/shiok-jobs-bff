package com.mtech.sj.bff.util
import kotlinx.serialization.Serializable

@Serializable
data class JwtHeaderDto(
        val kid: String,
        val alg: String
)