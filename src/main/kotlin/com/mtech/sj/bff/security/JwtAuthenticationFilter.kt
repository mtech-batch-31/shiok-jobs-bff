package com.mtech.sj.bff.security

import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter : OncePerRequestFilter() {
    private val secretKey = "your_jwt_secret_key"
    private val secretKeyBytes = secretKey.toByteArray(Charsets.UTF_8)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = extractTokenFromRequest(request)

        if (token != null && validateToken(token)) {
            val authentication = getAuthentication(token)
            if (authentication != null && authentication.authorities.any { it.authority == "ROLE_jobSeeker" }) {
                SecurityContextHolder.getContext().authentication = authentication
                filterChain.doFilter(request, response)
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient privileges")
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization") ?: return null
        if (!bearerToken.startsWith("Bearer ")) return null
        return bearerToken.substring(7)
    }

    private fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(secretKeyBytes).build()
            return true
        } catch (e: Exception) {
        }
        return false
    }

    private fun getAuthentication(token: String): Authentication? {
        val claims = Jwts.parserBuilder().setSigningKey(secretKeyBytes).build().parseClaimsJws(token).body
        val authorities = (claims["roles"] as List<*>).map { SimpleGrantedAuthority(it as String) }
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }
}
