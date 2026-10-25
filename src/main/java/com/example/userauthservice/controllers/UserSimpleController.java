package com.example.userauthservice.controllers;

import com.example.userauthservice.dtos.UserDto;
import com.example.userauthservice.models.User;
import com.example.userauthservice.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserSimpleController {

    private final UserRepo userRepo;

    @GetMapping("/users/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        Optional<User>  optionalUser = userRepo.findByEmail(authentication.getName());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDto dto = new UserDto();
        dto.setEmail(optionalUser.get().getEmail());
        dto.setRole(optionalUser.get().getRole());
        return ResponseEntity.ok(dto);
    }
}
