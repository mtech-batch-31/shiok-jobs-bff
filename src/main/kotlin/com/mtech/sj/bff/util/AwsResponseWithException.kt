package com.mtech.sj.bff.util

import com.mtech.sj.bff.exception.AwsException
import org.springframework.http.HttpStatus
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException

fun <T> withAwsException(block: () -> T) =
    try {
        block()
    } catch (e: CognitoIdentityProviderException) {
        throw AwsException(e.message!!, HttpStatus.valueOf(e.statusCode()))
    }
