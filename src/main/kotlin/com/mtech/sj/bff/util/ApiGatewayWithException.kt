package com.mtech.sj.bff.util

import com.mtech.sj.bff.exception.AppException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

fun withApiGatewayException(block: () -> Mono<ServerResponse>) =
    try {
        block()
    } catch (ex: AppException) {
        createServerResponse(ex.httpStatusCode, HttpHeaders.EMPTY, mapOf("error" to ex.message))
    } catch (ex: Exception) {
        createServerResponse(INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, mapOf("error" to ex.message))
    }
