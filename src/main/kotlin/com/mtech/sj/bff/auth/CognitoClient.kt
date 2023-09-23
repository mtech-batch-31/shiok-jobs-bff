package com.mtech.sj.bff.auth

import com.mtech.sj.bff.auth.dto.TokenResponse
import com.mtech.sj.bff.config.AppConfig
import com.mtech.sj.bff.util.withAwsException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType.REFRESH_TOKEN_AUTH
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType.USER_PASSWORD_AUTH
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Repository
class CognitoClient(
    private val cognitoIdentityProviderClient: CognitoIdentityProviderClient,
    @Qualifier("appConfig") config: AppConfig
) {

    private val clientId = config.aws.cognito.clientId
    private val clientSecret = config.aws.cognito.clientSecret

    fun register(email: String, password: String) =
        withAwsException {
            cognitoIdentityProviderClient.signUp(
                SignUpRequest.builder()
                    .clientId(clientId)
                    .secretHash(calculateSecretHash(clientId, clientSecret, email))
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
        }

    fun login(email: String, password: String) =
        withAwsException {
            cognitoIdentityProviderClient.initiateAuth(
                InitiateAuthRequest.builder()
                    .authFlow(USER_PASSWORD_AUTH)
                    .authParameters(
                        mapOf(
                            "USERNAME" to email,
                            "PASSWORD" to password,
                            "SECRET_HASH" to calculateSecretHash(clientId, clientSecret, email)
                        )
                    )
                    .clientId(clientId)
                    .build()
            )
                .authenticationResult()
                .run { TokenResponse(idToken(), accessToken(), refreshToken()) }
        }

    fun refreshToken(refreshToken: String) =
        withAwsException {
            cognitoIdentityProviderClient.initiateAuth(
                InitiateAuthRequest.builder()
                    .authFlow(REFRESH_TOKEN_AUTH)
                    .authParameters(mapOf("REFRESH_TOKEN" to refreshToken))
                    .clientId(clientId)
                    .build()
            )
                .authenticationResult()
                .run { TokenResponse(idToken(), accessToken(), refreshToken()) }
        }

    private fun calculateSecretHash(userPoolClientId: String, userPoolClientSecret: String, userName: String): String {
        return Mac.getInstance(MACSHA_256_ALGORITHM)
            .apply {
                init(
                    SecretKeySpec(
                        userPoolClientSecret.toByteArray(StandardCharsets.UTF_8),
                        MACSHA_256_ALGORITHM
                    )
                )
                update(userName.toByteArray(StandardCharsets.UTF_8))
            }
            .run {
                doFinal(userPoolClientId.toByteArray(StandardCharsets.UTF_8))
                    .let { Base64.getEncoder().encodeToString(it) }
            }
    }

    private companion object {
        private const val MACSHA_256_ALGORITHM = "HmacSHA256"
    }
}
