package com.tericcabrel.authapi.dtos;

import lombok.Getter;

public class LoginUserDto {
    @Getter
    private String email;
    private String userName;
    @Getter
    private String password;

    public LoginUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginUserDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public LoginUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "LoginUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
