package com.nmtrails.appcontest.payload.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class HandlePasswordResetRequest {

    @NotBlank
    private String password;

    @NotNull
    private Long userId;

    @NotBlank
    private String token;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
