package com.mtech.sj.bff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region.AP_SOUTHEAST_1
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient

@Configuration
class AWSConfiguration {

    @Bean
    fun cognitoClient(): CognitoIdentityProviderClient =
        CognitoIdentityProviderClient.builder()
            .region(AP_SOUTHEAST_1)
            .build()
}
