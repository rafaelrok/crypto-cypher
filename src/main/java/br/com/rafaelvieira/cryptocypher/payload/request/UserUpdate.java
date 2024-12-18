package br.com.rafaelvieira.cryptocypher.payload.request;


import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.annotation.Nullable;

@Data
@JsonTypeName("user")
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
}
