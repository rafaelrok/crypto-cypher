package br.com.rafaelvieira.cryptocypher.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class UserAuthentication {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
