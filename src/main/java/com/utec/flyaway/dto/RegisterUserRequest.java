package com.utec.flyaway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotBlank(message = "El nombre es obligatorio")
        @Pattern(regexp = ".*[A-Z].*", message = "El nombre debe tener al menos 1 letra mayúscula")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Pattern(regexp = ".*[A-Z].*", message = "El apellido debe tener al menos 1 letra mayúscula")
        String lastName,

        @NotBlank(message = "La contraseña es obligatoria")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "La contraseña debe tener mínimo 8 caracteres, al menos 1 letra y 1 número"
        )
        String password
) {
}
