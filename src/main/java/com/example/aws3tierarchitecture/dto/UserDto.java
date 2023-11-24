package com.example.aws3tierarchitecture.dto;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String state;
    private int money;


    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .state(state)
                .money(money)
                .build();
    }
}