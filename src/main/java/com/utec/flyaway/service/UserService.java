package com.utec.flyaway.service;

import com.utec.flyaway.domain.User;
import com.utec.flyaway.dto.RegisterUserRequest;
import com.utec.flyaway.dto.UserResponse;
import com.utec.flyaway.exception.ConflictException;
import com.utec.flyaway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Ya existe un usuario registrado con ese email");
        }

        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId());
    }
}
