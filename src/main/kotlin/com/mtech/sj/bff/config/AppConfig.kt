package com.mtech.sj.bff.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppConfig {
    val services = ServiceConfig()
    val aws = AWSConfig()
}

class ServiceConfig {
    lateinit var frontend: String
    val backend: List<BackendService> = ArrayList()
}

class BackendService {
    lateinit var name: String
    lateinit var url: String
}

class AWSConfig {
    val cognito = CognitoConfig()
}

class CognitoConfig {
    lateinit var clientId: String
    lateinit var clientSecret: String
}
