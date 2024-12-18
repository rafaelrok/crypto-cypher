package br.com.rafaelvieira.cryptocypher.mapper;

import br.com.rafaelvieira.cryptocypher.domain.user.User;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.payload.request.UserUpdate;
import br.com.rafaelvieira.cryptocypher.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(role -> role.getName().name()).toList())
                .build();
    }

    public User toEntity(UserRegister register) {
        return modelMapper.map(register, User.class);
    }

    public void copyToEntity(UserUpdate update, User user) {
        modelMapper.map(update, user);
    }
}
