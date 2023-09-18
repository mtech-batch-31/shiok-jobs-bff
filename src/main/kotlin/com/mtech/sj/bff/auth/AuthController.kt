package com.mtech.sj.bff.auth

import com.mtech.sj.bff.auth.dto.LoginRequest
import com.mtech.sj.bff.auth.dto.RefreshTokenRequest
import com.mtech.sj.bff.auth.dto.RegisterRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class AuthController(private val cognitoClient: CognitoClient) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest) =
        Mono.fromCallable { cognitoClient.register(request.username, request.password) }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) =
        Mono.fromCallable { cognitoClient.login(request.username, request.password) }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest) =
        Mono.fromCallable { cognitoClient.refreshToken(request.refreshToken) }
}
