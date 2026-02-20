package com.codewithdurgesh.blog.payloads;

public class JwtAuthResponse {

    private String token;

    public JwtAuthResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
