package com.token.dto;

public class TokenResponse {
    private String jwt;
    private String body;

    public TokenResponse() {
    }

    public TokenResponse(String jwt) {
        this.jwt = jwt;
    }

    public TokenResponse(String jwt, String body) {
        this.jwt = jwt;
        this.body = body;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
