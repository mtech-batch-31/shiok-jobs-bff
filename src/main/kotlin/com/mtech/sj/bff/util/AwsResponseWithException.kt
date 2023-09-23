package com.mtech.sj.bff.util

import com.mtech.sj.bff.exception.AwsException
import org.springframework.http.HttpStatusCode
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException

fun <T> withAwsException(block: () -> T): T {
    try {
        return block()
    } catch (e: CognitoIdentityProviderException) {
        throw AwsException(e.message!!, HttpStatusCode.valueOf(e.statusCode()))
    }
}
