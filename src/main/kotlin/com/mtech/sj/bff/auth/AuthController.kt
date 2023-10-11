package com.mtech.sj.bff.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.mtech.sj.bff.auth.dto.LoginRequest
import com.mtech.sj.bff.auth.dto.RefreshTokenRequest
import com.mtech.sj.bff.auth.dto.RegisterRequest
import com.mtech.sj.bff.security.AwsCognitoRSAKeyProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@Suppress("lint")
@RestController
@RequestMapping("/api/auth")
class AuthController(private val cognitoClient: CognitoClient) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest) =
        Mono.fromCallable { cognitoClient.register(request.email, request.password) }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) =
        Mono.fromCallable { cognitoClient.login(request.email, request.password) }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest) =
        Mono.fromCallable { cognitoClient.refreshToken(request.refreshToken) }

//    @GetMapping("/verifyToken")
//    fun verifyToken(@RequestBody request: RefreshTokenRequest) =
//            Mono.fromCallable { cognitoClient.refreshToken(request.refreshToken) }

//    @GetMapping("/verifyToken")
//    fun verifyToken(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<String> {
////        val username = jwt.subject
////        val response = mapOf("message" to "Token verified for user: $Jwts.")
//        try{
////            var response = authorizationHeader;
//            val token = authorizationHeader.replace("Bearer ", "")
//            cognitoClient.verifyToken()
//            return ResponseEntity.ok("Token verified for user: ${token}")
//        } catch (e:Exception){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed: ${e.message}")
//        }
//
//
//    }

    @GetMapping("/verifyToken")
    fun verifyToken(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<String> {
//        val username = jwt.subject
//        val response = mapOf("message" to "Token verified for user: $Jwts.")
        try{
//            var response = authorizationHeader;
            val token = authorizationHeader.replace("Bearer ", "")
//            cognitoClient.verifyToken()

            val aws_cognito_region = "" // Replace this with your aws cognito region

            val aws_user_pools_id = "" // Replace this with your aws user pools id

            val keyProvider: RSAKeyProvider = AwsCognitoRSAKeyProvider(aws_cognito_region, aws_user_pools_id)
            val algorithm: Algorithm = Algorithm.RSA256(keyProvider)
            val jwtVerifier: JWTVerifier = JWT.require(algorithm) //.withAudience("2qm9sgg2kh21masuas88vjc9se") // Validate your apps audience if needed
                    .build()
//            val token = "eyJraWQiOiJjdE.eyJzdWIiOiI5NTMxN2E.VX819z1A1rJij2" // Replace this with your JWT token
            jwtVerifier.verify(token)
            return ResponseEntity.ok("Token verified for user: ${token}")
        } catch (e:Exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed: ${e.message}")
        }


    }

//    @GetMapping("/verifyToken")
//    fun verifyToken(@AuthenticationPrincipal jwt: Jwt): Mono<Map<String, Any>> {
//        // Access JWT claims here
//        val claims = jwt.claims
//        return Mono.just(claims)
//    }
}
