package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatusCode
abstract class AppException(
    message: String,
    public val httpStatusCode: HttpStatusCode
) : RuntimeException(message)
