package br.com.rafaelvieira.cryptocypher.payload.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@JsonTypeName("user")
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
}
