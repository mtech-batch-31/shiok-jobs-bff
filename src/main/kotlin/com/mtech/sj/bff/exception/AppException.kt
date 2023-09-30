package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatus
abstract class AppException(
    message: String,
    val httpStatusCode: HttpStatus
) : RuntimeException(message)
