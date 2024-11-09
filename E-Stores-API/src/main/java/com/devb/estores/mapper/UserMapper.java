package com.devb.estores.mapper;

import com.devb.estores.enums.UserRole;
import com.devb.estores.model.User;
import com.devb.estores.requestdto.UserRequest;
import com.devb.estores.responsedto.UserResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserMapper {

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(UserRole::name).toList())
                .email(user.getEmail())
                .isEmailVerified(user.isEmailVerified())
                .build();
    }

    public User mapToUserEntity(UserRequest userRequest, UserRole role) {
        return User.builder()
                .username(userRequest.getEmail().split("@gmail.com")[0])
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .roles(role.equals(UserRole.SELLER)
                        ? Arrays.asList(UserRole.SELLER, UserRole.CUSTOMER)
                        : List.of(UserRole.CUSTOMER))
                .build();
    }
}
