package com.mtech.sj.bff.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppConfiguration {
    val aws = AWSConfig()
}
class AWSConfig {
    val cognito = CognitoConfig()
}

class CognitoConfig {
    lateinit var clientId: String
}
