package com.mtech.sj.bff.util

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse

fun createServerResponse(response: ResponseEntity<String>) =
    createServerResponse(HttpStatus.valueOf(response.statusCode.value()), response.headers, response.body)

fun createServerResponse(status: HttpStatus, headers: HttpHeaders, body: Any?) =
    ServerResponse.status(status)
        .apply { headers.forEach { name, values -> values.forEach { value -> header(name, value) } } }
        .run { body?.let { body(BodyInserters.fromValue(it)) } ?: build() }
