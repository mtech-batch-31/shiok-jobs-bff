package com.mtech.sj.bff.util

import com.mtech.sj.bff.exception.AwsException
import com.mtech.sj.bff.exception.UnauthorizedException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException

val logger: Logger = LoggerFactory.getLogger("com.mtech.sj.bff.util.AwsResponseWithExceptionKt")

fun <T> withAwsException(block: () -> T) =
    try {
        block()
    } catch (e: CognitoIdentityProviderException) {
        logger.error("CognitoIdentityProviderException: ${e.message}")
        if (e.message?.contains("Invalid Access Token") == true) {
            throw UnauthorizedException("Invalid Access Token")
        } else if (e.message?.contains("Access Token has expired") == true) {
            // TODO: auto refresh token
            throw UnauthorizedException("Access Token has expired")
        } else {
            throw AwsException(e.message!!, HttpStatus.valueOf(e.statusCode()))
        }
    }
