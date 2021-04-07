package com.token.controller;

import com.token.dto.TokenResponse;
import com.token.service.ITokenService;
import com.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TokenController {

    @Autowired
    ITokenService iTokenService;

    @PostMapping("/api/token")
    @ResponseBody
    public String generateToken(@RequestHeader("X-Algorithm") String alg,
                                       @RequestHeader ("X-Secret") String secret,
                                       @RequestBody String body) {
        try {
            return iTokenService.generateToken(alg, secret,body).getJwt();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getCause().getMessage());
        }
    }

    @GetMapping("/api/token")
    @ResponseBody
    public String  decodeJWT(@RequestHeader("X-Algorithm") String alg,
                                 @RequestHeader("X-Secret") String secret,
                                 @RequestHeader("X-JWT") String jwt){
        try {
            return iTokenService.decodeToken(alg,secret,jwt).getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
