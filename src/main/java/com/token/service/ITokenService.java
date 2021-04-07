package com.token.service;

import com.token.dto.TokenResponse;

public interface ITokenService {
    TokenResponse generateToken(String alg, String secret, String body) throws Exception;
    TokenResponse decodeToken(String alg,String secret,String jwt) throws Exception;
}
