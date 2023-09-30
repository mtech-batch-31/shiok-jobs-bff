package com.mtech.sj.bff.config

import com.mtech.sj.bff.apiGateway.ForwardingClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfig(
    private val forwardingClient: ForwardingClient,
    private val appConfig: AppConfig
) {
    @Bean
    fun routerFunction() = router {
        appConfig.services.backend.forEach { backendService ->
            backendService.name.let { service ->
                path("/api/$service/**") { request ->
                    forwardingClient.forwardRequest(service, request)
                }
            }
        }
    }
}
