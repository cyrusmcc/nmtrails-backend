package com.nmtrails.appcontest.payload.requests;

import javax.validation.constraints.NotBlank;

public class PasswordChangeRequest {

    @NotBlank
    String currentPassword;

    @NotBlank
    String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}