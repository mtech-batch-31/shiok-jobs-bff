package com.mtech.sj.bff.util;

import kotlinx.serialization.json.Json;
//import kotlinx.serialization.decodeFromString;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtUtilJava {

    public static Map<String,String> getDecryptedJwtFromJwt(String jwt){
        Map<String, String> decrypted = new HashMap<>();
        try {
            String token = jwt.replace("Bearer ", "");
            System.out.println("token " + token);
            List<String> chunks = List.of(token.split("\\."));
            Base64.Decoder decoder = Base64.getDecoder();
            System.out.println("chunk0 " + chunks.get(0));
            System.out.println("chunk1 " + chunks.get(1));
            String header = new String(decoder.decode(chunks.get(0)));
            System.out.println("header " + header);
            String payload = new String(decoder.decode(chunks.get(1)));
            System.out.println("payload " + payload);

            String signature = new String(decoder.decode(chunks.get(2)));
            System.out.println("signature " + signature);

//            val myData = Json.decodeFromString<> (JwtHeaderDto.class, header);
//            (header)
            decrypted.put("header", header);
            decrypted.put("payload", payload);
            decrypted.put("signature", signature);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
