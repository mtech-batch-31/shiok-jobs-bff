package com.mtech.sj.bff.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class CognitoJwtVerifier(
        val region: String,
        val userPoolId: String,
        val clientId: String
) {



//    fun verifyJwt(token: String): Boolean {
//        try {
//            // Decode the JWT token (You can use libraries like Nimbus JWT to decode)
//            val jwtToken = decodeJwt(token)
//
//            // Verify the token issuer (Cognito User Pool)
//            if (jwtToken.issuer != "https://cognito-idp.$region.amazonaws.com/$userPoolId") {
//                return false
//            }
//
//            // Verify the audience (Client ID)
//            if (!jwtToken.audience.contains(clientId)) {
//                return false
//            }
//
//            // Additional verification checks can be added here
//
//            // If all checks pass, the token is valid
//            return true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return false
//        }
//    }
//
//    private fun decodeJwt(token: String){
//        // Implement JWT token decoding logic (e.g., using Nimbus JWT)
//        // Return the decoded JWT
//    }
}