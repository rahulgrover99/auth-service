package com.example.userauthservice.dtos;

import com.example.userauthservice.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private Role role;
}
