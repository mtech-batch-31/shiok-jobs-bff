package com.mtech.sj.bff.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig @Autowired constructor(private val appConfig: AppConfig) {
    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http.securityMatcher(
            PathPatternParserServerWebExchangeMatcher("/api/**")
        )
            .authorizeExchange {
                it.pathMatchers("/api/auth/**")
                    .permitAll()
                it.pathMatchers("/api/**")
                    .hasRole("jobSeeker")
                    .anyExchange()
                    .authenticated()
            }
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .build()

    private fun corsConfigurationSource(): CorsConfigurationSource {
        logger.info("appConfig.service.frontend is " + appConfig.service.frontend)

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration(
                "/api/**",
                CorsConfiguration().apply {
                    allowedOrigins = listOf(appConfig.service.frontend)
                    allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    allowCredentials = true
                    allowedHeaders = listOf("*")
                })
        }
    }
}
