package br.com.rafaelvieira.cryptocypher.payload.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseCookie;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    private String token;                  // Mantemos para compatibilidade
    private ResponseCookie tokenCookie;    // Novo campo para o cookie
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean isFirstAccess;
}
