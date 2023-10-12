package com.mtech.sj.bff.apiGateway

import com.mtech.sj.bff.exception.ServiceNotFoundException
import com.mtech.sj.bff.util.createServerResponse
import com.mtech.sj.bff.util.withApiGatewayException
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.bodyToMono
import java.net.URI
import java.net.http.HttpHeaders

@Repository
class ForwardingClient(private val webClients: Map<String, WebClient>) {
    fun forwardRequest(serviceName: String, originalRequest: ServerRequest) =
        withApiGatewayException {
            webClients[serviceName]
                .also { it ?: throw ServiceNotFoundException("Service $serviceName is not found") }!!
                .method(originalRequest.method())
                .uri(parserUri(originalRequest.uri(), serviceName))
                .headers { it.addAll(originalRequest.headers().asHttpHeaders()) }
                .body(originalRequest.bodyToMono<String>(), String::class.java)
                .exchangeToMono { it.toEntity<String>() }
                .flatMap { createServerResponse(it) }
        }

    private fun parserUri(originalUri: URI, serviceName: String) =
        "${originalUri.path.replaceFirst("/api/$serviceName", "")}${originalUri.query?.let { "?$it" } ?: ""}"

//    private fun addUserUuidHeader(headers : HttpHeaders) : HttpHeaders {
//        String token = headers.firstValue("Authorization")
//    }



}
