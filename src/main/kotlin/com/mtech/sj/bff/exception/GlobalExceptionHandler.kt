package com.mtech.sj.bff.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import reactor.core.publisher.Mono

@ControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException) =
        Mono.just(
            ExceptionResponse(
                ex.bindingResult
                    .fieldErrors
                    .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            )
        )

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeExceptions(ex: RuntimeException) =
        Mono.just(ExceptionResponse(ex.message ?: "An error occurred."))
}
