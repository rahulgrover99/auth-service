package com.example.userauthservice.config;

import com.example.userauthservice.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
               String header = request.getHeader("Authorization");
               if (header != null && header.startsWith("Bearer ")) {
                   try {
                       Claims claims = jwtService.parseToken(header.substring("Bearer ".length()));
                       List<SimpleGrantedAuthority> authorities = List.of(
                               new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class))
                       );
                       Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                       SecurityContextHolder.getContext().setAuthentication(authentication);
                   } catch (JwtException e) {
                       SecurityContextHolder.clearContext();
                   }
               }
               filterChain.doFilter(request, response);
        }
}
