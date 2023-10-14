package com.mtech.sj.bff.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mtech.sj.bff.security.JwtPayload
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

val logger: Logger = LoggerFactory.getLogger(JwtPayload::class.java)
@Component
class JwtAuthenticationFilter(
    private val cognitoClient: CognitoClient,
    private val objectMapper: ObjectMapper
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request

        val idToken = request.headers.getFirst("x-id-token")
        val accessToken = request.headers.getFirst("authorization")?.replace("Bearer ", "")

        logger.info("idToken: $idToken")

        accessToken?.also { cognitoClient.validate(it) }

        return chain.filter(idToken?.let {
            exchange.mutate()
                .request(addHeadersToRequest(request, parseIdToken(it).toMap()))
                .build()
        } ?: exchange)
    }

    private fun parseIdToken(idToken: String): JwtPayload {
        val splitToken = idToken.split('.')
        if (splitToken.size != 3) {
            throw IllegalArgumentException("Invalid JWT token")
        }
        val tokenBody = splitToken[1] // 获取 payload

        logger.info("tokenBody: $tokenBody")

        val decodedJson = String(Base64.getUrlDecoder().decode(tokenBody))

        return objectMapper.readValue(decodedJson)
    }

    private fun addHeadersToRequest(request: ServerHttpRequest, headers: Map<String, String?>): ServerHttpRequest {
        return request.mutate().headers { httpHeaders ->
            headers.forEach { (name, value) ->
                if (value != null) {
                    httpHeaders.set(name, value)
                }
            }
        }.build()
    }
}

