package com.example.userauthservice.services;

import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.User;
import com.example.userauthservice.repositories.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepo userRepo, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
}
