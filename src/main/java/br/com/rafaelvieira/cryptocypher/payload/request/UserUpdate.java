package br.com.rafaelvieira.cryptocypher.payload.request;


import br.com.rafaelvieira.cryptocypher.util.FxTypeName;
import jakarta.validation.constraints.Email;
import jakarta.annotation.Nullable;


@FxTypeName("user")
public class UserUpdate {

    @Nullable
    private String fullName;

    @Nullable
    private String username;

    @Email
    @Nullable
    private String email;

    @Nullable
    private String password;

    public UserUpdate(@Nullable String fullName,
                      @Nullable String username,
                      @Nullable String email,
                      @Nullable String password) {
        this.fullName = fullName;
        this.username = username;
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
}
