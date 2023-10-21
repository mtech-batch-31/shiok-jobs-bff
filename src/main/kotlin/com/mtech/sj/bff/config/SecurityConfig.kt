package com.mtech.sj.bff.config

import com.mtech.sj.bff.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    private val appConfig: AppConfig,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http.securityMatcher(
            PathPatternParserServerWebExchangeMatcher(
                "/api/**"
            )
        )
            .addFilterAt(jwtAuthenticationFilter, AUTHENTICATION)
            .authorizeExchange {
                it.pathMatchers("/api/user/**", "/api/job/apply")
                    .authenticated()

                it.pathMatchers("/api/auth/**", "/api/test", "/api/job/**")
                    .permitAll()
            }
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .build()

    private fun corsConfigurationSource() =
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration(
                "/api/**",
                CorsConfiguration().apply {
                    allowedOrigins = listOf(appConfig.services.frontend)
                    allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    allowCredentials = true
                    allowedHeaders = listOf("*")
                }
            )
        }
}
