package com.mtech.sj.bff.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mtech.sj.bff.auth.CognitoClient
import com.mtech.sj.bff.exception.UnauthorizedException
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val cognitoClient: CognitoClient,
    private val objectMapper: ObjectMapper
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request

        val idToken = request.headers.getFirst("x-id-token")
        val accessToken = request.headers.getFirst("Authorization")?.replace("Bearer ", "")

        accessToken?.also { cognitoClient.validate(it) }

        if (accessToken == null) {
            throw UnauthorizedException("Missing access token")
        }

        val headers = parseIdToken(idToken.toString())
        // Assuming authorities for jobSeeker are assigned based on user details.
        val authorities = listOf(SimpleGrantedAuthority("jobSeeker"))

        val authentication = UsernamePasswordAuthenticationToken(headers.email, null, authorities)
        val context = ReactiveSecurityContextHolder.withAuthentication(authentication)

        return chain.filter(
            idToken?.let {
                exchange.mutate()
                    .request(addHeadersToRequest(request, headers.toMap()))
                    .build()
            } ?: exchange
        ).contextWrite(context)
    }

    private fun parseIdToken(idToken: String): JwtPayload {
        val splitToken = idToken.split('.')
        if (splitToken.size != 3) {
            throw UnauthorizedException("Invalid JWT token")
        }
        val tokenBody = splitToken[1] // 获取 payload

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
