package com.mtech.sj.bff.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mtech.sj.bff.auth.CognitoClient
import com.mtech.sj.bff.security.JwtPayload.Companion.parseIdToken
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

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

        return chain.filter(
            idToken?.let {
                exchange.mutate()
                    .request(addHeadersToRequest(request, parseIdToken(idToken.toString(), objectMapper).toMap()))
                    .build()
            } ?: exchange
        ).contextWrite { context ->
            idToken?.let {
                val email = parseIdToken(it, objectMapper).email
                val authorities = listOf(SimpleGrantedAuthority("jobSeeker"))
                val authentication = UsernamePasswordAuthenticationToken(email, null, authorities)
                ReactiveSecurityContextHolder.withAuthentication(authentication)
            } ?: context
        }
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
