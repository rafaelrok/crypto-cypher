package br.com.rafaelvieira.cryptocypher.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class UserAuthentication {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UserAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
