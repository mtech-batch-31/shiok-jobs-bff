package com.mtech.sj.bff.util

import java.util.*
import java.util.List
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
class JwtUtil {

    companion object{
        fun getDecryptedJwtFromJwt(jwt: String): Map<String, String>? {
            val decrypted: MutableMap<String, String> = HashMap()
            return try {
                val token = jwt.replace("Bearer ", "")
                println("token $token")
                val chunks = List.of(*token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                val decoder = Base64.getDecoder()
                println("chunk0 " + chunks[0])
                println("chunk1 " + chunks[1])
                println("chunk2 " + chunks[2])

                val header = String(decoder.decode(chunks[0]))
                println("header $header")

                val payload = String(decoder.decode(chunks[1]))
                println("payload $payload")
                val mapper = jacksonObjectMapper()
//                val userFromJsonWithType = mapper.readValue(payload, JwtPayloadDto.class)
//                val signature = String(decoder.decode(chunks[2]))
//                println("signature $signature")
//            val myData = Json.decodeFromString<JwtHeaderDto.class>( header);
                decrypted["header"] = header
                decrypted["payload"] = payload
                decrypted["signature"] = chunks[2]
                return decrypted
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}