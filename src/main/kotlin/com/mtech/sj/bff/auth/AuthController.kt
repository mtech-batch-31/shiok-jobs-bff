package com.mtech.sj.bff.auth

import com.mtech.sj.bff.auth.dto.LoginRequest
import com.mtech.sj.bff.auth.dto.RegisterRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class AuthController(private val cognitoClient: CognitoClient) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest) =
        Mono.fromCallable { cognitoClient.register(request.email, request.password) }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) =
        Mono.fromCallable { cognitoClient.login(request.email, request.password) }

    @GetMapping("/refresh")
    fun refreshToken(@RequestHeader("x-refresh-token") refreshToken: String) =
        Mono.fromCallable { cognitoClient.refreshToken(refreshToken) }

    @GetMapping("/logout")
    fun logout(@RequestHeader refreshToken: String) =
        Mono.fromCallable { cognitoClient.logout(refreshToken) }

//    @GetMapping("/validate")
//    fun validate(@RequestHeader(name = "authorization") accessToken: String) =
//        Mono.fromCallable { cognitoClient.validate(accessToken.replace("Bearer", "")) }
}
