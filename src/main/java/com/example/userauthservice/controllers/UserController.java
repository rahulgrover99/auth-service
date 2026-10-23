package com.example.userauthservice.controllers;

import com.example.userauthservice.dtos.AuthRequest;
import com.example.userauthservice.dtos.LoginResponse;
import com.example.userauthservice.dtos.UserDto;
import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.User;
import com.example.userauthservice.services.AuthService;
import com.example.userauthservice.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final AuthService authService;
    private final JwtService jwtService;

    public UserController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        User createdUser = authService.signUp(user.getEmail(), user.getPassHash());
        if (createdUser == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String header) {
        if (!header.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = jwtService.parseToken(header.substring("Bearer ".length()));
            UserDto dto = new UserDto();
            dto.setEmail(claims.getSubject());
            dto.setRole(Role.valueOf(claims.get("role", String.class)));
            return ResponseEntity.ok(dto);
        } catch (JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
