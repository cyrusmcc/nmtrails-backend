package com.nmtrails.appcontest.payload.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class HandleChangeEmailRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String token;

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
