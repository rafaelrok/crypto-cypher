package br.com.rafaelvieira.cryptocypher.payload.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@JsonTypeName("user")
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private List<String> roles;
}
