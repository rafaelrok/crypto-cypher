package br.com.rafaelvieira.cryptocypher.payload.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("user")
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private List<String> roles;

    public UserResponse(Long id, String username, String fullName, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
