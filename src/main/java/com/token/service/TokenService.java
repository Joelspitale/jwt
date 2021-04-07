package com.token.service;

import com.token.dto.TokenResponse;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class TokenService implements ITokenService {
    public TokenResponse generateToken(String alg, String secret, String body) throws Exception {

        String headerB64 = getHeaderB64(alg);
        String payloadB64 = getPayloadB64(body);
        String jwt = createJwt(alg,headerB64, payloadB64, secret );
        return new TokenResponse(jwt);
    };

    public TokenResponse decodeToken(String alg,String secret,String jwt) throws Exception {
        String[] partsToken = jwt.split("\\.");
        boolean isEquals = false;

        if(getSignature(alg, partsToken[0],partsToken[1], secret).equals(partsToken[2]))
            isEquals =true ;

        String payload = new String(Base64.getDecoder().decode(partsToken[1]));

        String body = ("{ \n \"payload\": "+ payload +",\n \"signature\":"+ isEquals +"}" );

        return new TokenResponse(jwt,body);
    }

    private String  getHeaderB64(String alg) {
        String header = "{\"alg\":\"" + alg + "\",\n \"typ\": \"JWT\" \n } ";
        return getB64Encode(header.getBytes(StandardCharsets.UTF_8));
    }

    private String getPayloadB64(String body){
       return getB64Encode(body.getBytes(StandardCharsets.UTF_8));
    }

    private String createJwt(String alg, String headerB64, String payloadB64, String secret) throws Exception {
        String hp = headerB64 + "." + payloadB64;
        String jwt = "{ \n \"jwt\": "+ hp +"."+ getSignature(alg, headerB64,payloadB64,secret)+ "\" } \n";
        return  jwt;
    }

    private String getSignature(String alg, String headerB64, String payloadB64, String secret) throws Exception {
        String algorithm;
        switch(alg){
            case "HS256":
                algorithm = "HmacSHA256";
                break;

            default:
                algorithm = null;
                throw new Exception("Algoritmo Invalido");
        }
        String hp = null;
        String signatureB64 = null ;

        try {
            hp = headerB64 + "." + payloadB64;
            Mac hmac = Mac.getInstance(algorithm);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm);
            hmac.init(secret_key);
            signatureB64 = getB64Encode(hmac.doFinal(hp.getBytes("UTF-8"))) ;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }

       return signatureB64;
    }

    private String getB64Encode(byte [] bytes){
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
