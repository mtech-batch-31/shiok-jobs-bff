package com.mtech.sj.bff.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.json.JsonParseException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException
import java.net.BindException


val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        IllegalArgumentException::class,
        BindException::class,
        JsonParseException::class,
        IllegalStateException::class,
        MethodArgumentTypeMismatchException::class
    )
    fun handleValidationExceptions(ex: Exception): Mono<ExceptionResponse> {
        val errorMassage = when (ex) {
            is MethodArgumentNotValidException -> ex.bindingResult
                .fieldErrors
                .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

            else -> {
                ex.message.orEmpty()
            }
        }
        return Mono.just(

            ExceptionResponse(errorMassage)
        ).also { logger.warn(ex.message, ex) }
    }

    @ExceptionHandler(CognitoIdentityProviderException::class)
    fun handleInvalidParameterException(ex: CognitoIdentityProviderException): Mono<ExceptionResponse> =
        Mono.just(ExceptionResponse(ex.message ?: "An error occurred."))
            .also { logger.error("code is: " +ex.statusCode() + ex.message, ex)

            }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeExceptions(ex: RuntimeException) =
        Mono.just(ExceptionResponse(ex.message ?: "An error occurred."))
            .also { logger.error(ex.message, ex) }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleExceptions(ex: Exception): Mono<ExceptionResponse> =
        Mono.just(ExceptionResponse(ex.message ?: "An error occurred."))
            .also { logger.error(ex.message, ex) }
}
