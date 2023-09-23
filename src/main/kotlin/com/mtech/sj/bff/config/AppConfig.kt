package com.mtech.sj.bff.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppConfig {
    val service = ServiceConfig()
    val aws = AWSConfig()
}

class ServiceConfig {
    lateinit var frontend: String
}

class AWSConfig {
    val cognito = CognitoConfig()
}

class CognitoConfig {
    lateinit var clientId: String
    lateinit var clientSecret: String
}
