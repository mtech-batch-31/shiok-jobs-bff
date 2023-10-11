package com.mtech.sj.bff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(private val appConfig: AppConfig) {
    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http.securityMatcher(
            PathPatternParserServerWebExchangeMatcher(
                "/api/**"
            )
        )
            .authorizeExchange {
                it.pathMatchers("/api/auth/**", "/api/test", "/api/job/**")
                    .permitAll()
                it.pathMatchers("/api/**")
                    .hasRole("jobSeeker")
                    .anyExchange()
                    .authenticated()
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


//    @Bean
//    fun jwtDecoder(): NimbusReactiveJwtDecoder {
//        // Configure your JWT decoder here (e.g., JWKS URL)
//        return NimbusReactiveJwtDecoder
//                .withJwkSetUri("https://cognito-idp..amazonaws.com//.well-known/jwks.json")
//                .build()
//    }

//    @Bean
//    fun jwtDecoder(): ReactiveJwtDecoder {
//        // Configure your JWT decoder here (e.g., JWKS URL)
//        return ReactiveJwtDecoders.fromIssuerLocation("https://cognito-idp..amazonaws.com//.well-known/jwks.json")
//    }
//    @Bean
//    fun jwtReactiveAuthenticationManager(): JwtReactiveAuthenticationManager {
//        return JwtReactiveAuthenticationManager(jwtDecoder())
//    }
}
