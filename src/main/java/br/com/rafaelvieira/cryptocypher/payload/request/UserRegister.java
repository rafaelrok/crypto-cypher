package br.com.rafaelvieira.cryptocypher.payload.request;

import br.com.rafaelvieira.cryptocypher.util.FxTypeName;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;

import java.util.Set;

@FxTypeName("user")
public class UserRegister {

    @Nullable
    private String fullName;

    @Nullable
    private String username;

    @Email
    @Nullable
    private String email;

    @Nullable
    private String password;

    private Set<String> roles;

    public UserRegister(@Nullable String username,
                        @Nullable String fullName,
                        @Nullable String email,
                        @Nullable String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    @Nullable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@Nullable String fullName) {
        this.fullName = fullName;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
