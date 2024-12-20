package br.com.rafaelvieira.cryptocypher.mapper;

import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.payload.request.UserRegister;
import br.com.rafaelvieira.cryptocypher.payload.request.UserUpdate;
import br.com.rafaelvieira.cryptocypher.payload.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
//        return UserResponse.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .fullName(user.getFullName())
//                .email(user.getEmail())
//                .roles(user.getRoles().stream().map(role -> role.getName().name()).toList())
//                .build();
    }

    public User toEntity(UserRegister register) {
        return modelMapper.map(register, User.class);
    }

    public void copyToEntity(UserUpdate update, User user) {
        modelMapper.map(update, user);
    }
}
