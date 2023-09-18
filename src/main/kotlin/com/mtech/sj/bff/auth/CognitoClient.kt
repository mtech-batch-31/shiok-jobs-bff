package com.mtech.sj.bff.auth

import com.mtech.sj.bff.config.AppConfiguration
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType.REFRESH_TOKEN_AUTH
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType.USER_PASSWORD_AUTH
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse

@Repository
class CognitoClient(private val cognitoIdentityProviderClient: CognitoIdentityProviderClient, config: AppConfiguration) {

    private val clientId = config.aws.cognito.clientId

    fun register(email: String, password: String): SignUpResponse =
        cognitoIdentityProviderClient.signUp(
            SignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .password(password)
                .userAttributes(
                    AttributeType.builder()
                        .name("email")
                        .value(email)
                        .build()
                )
                .build()
        )

    fun login(username: String, password: String): InitiateAuthResponse =
        cognitoIdentityProviderClient.initiateAuth(
            InitiateAuthRequest.builder()
                .authFlow(USER_PASSWORD_AUTH)
                .authParameters(
                    mapOf(
                        "USERNAME" to username,
                        "PASSWORD" to password
                    )
                )
                .clientId(clientId)
                .build()
        )

    fun refreshToken(refreshToken: String): InitiateAuthResponse =
        cognitoIdentityProviderClient.initiateAuth(
            InitiateAuthRequest.builder()
                .authFlow(REFRESH_TOKEN_AUTH)
                .authParameters(mapOf("REFRESH_TOKEN" to refreshToken))
                .clientId(clientId)
                .build()
        )
}
