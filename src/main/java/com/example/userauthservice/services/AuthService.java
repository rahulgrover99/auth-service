package com.example.userauthservice.services;

import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.User;
import com.example.userauthservice.repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepo userRepo, PasswordEncoder bCryptPasswordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    public User signUp(String email, String pass) {
        if (userRepo.findByEmail(email).isPresent()) {
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassHash(bCryptPasswordEncoder.encode(pass));
        user.setRole(Role.CUSTOMER);
        return userRepo.save(user);
    }

    public String login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isEmpty()
                || !bCryptPasswordEncoder.matches(password, optionalUser.get().getPassHash())) {
            return null;
        }
        return jwtService.generateToken(optionalUser.get());
    }
}
