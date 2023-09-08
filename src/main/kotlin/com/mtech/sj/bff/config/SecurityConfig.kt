package com.mtech.sj.bff.config

import com.mtech.sj.bff.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain =
        http.run {
            authorizeHttpRequests {
                it.requestMatchers("/api/**").hasRole("USER")
                    .anyRequest().authenticated()
            }
            csrf { it.disable() }
            formLogin(withDefaults())
            addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            build()
        }
}
