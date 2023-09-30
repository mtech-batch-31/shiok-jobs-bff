package com.mtech.sj.bff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val appConfig: AppConfig,
    private val webClientBuilder: WebClient.Builder
) {

    @Bean
    fun webClients() = appConfig.services
        .backend
        .associateBy(
            { it.name },
            { webClientBuilder.baseUrl(it.url).build() }
        )
}
