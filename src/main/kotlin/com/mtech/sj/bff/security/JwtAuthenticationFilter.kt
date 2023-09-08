package com.mtech.sj.bff.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = extractTokenFromRequest(request)

        if (token != null && validateToken(token)) {
            val authentication = getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        return null
    }

    private fun validateToken(token: String): Boolean {
        return true
    }

    private fun getAuthentication(token: String): Authentication? {
        return null
    }
}

// data class Authentication(val name: String,val role:String)
