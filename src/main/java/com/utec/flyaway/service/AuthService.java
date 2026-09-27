package com.utec.flyaway.service;

import com.utec.flyaway.domain.User;
import com.utec.flyaway.dto.LoginRequest;
import com.utec.flyaway.dto.LoginResponse;
import com.utec.flyaway.exception.InvalidCredentialsException;
import com.utec.flyaway.repository.UserRepository;
import com.utec.flyaway.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}
