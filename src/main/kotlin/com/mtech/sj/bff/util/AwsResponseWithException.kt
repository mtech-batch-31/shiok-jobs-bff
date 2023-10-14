package com.mtech.sj.bff.util

import com.mtech.sj.bff.exception.AwsException
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
        if (e.message?.contains( "Invalid Access Token") == true)
            throw AwsException("Invalid Access Token", HttpStatus.UNAUTHORIZED)
        else if (e.message?.contains("Access Token has expired") == true)
            throw AwsException("Access Token has expired", HttpStatus.UNAUTHORIZED)
        else
            throw AwsException(e.message!!, HttpStatus.valueOf(e.statusCode()))
    }
