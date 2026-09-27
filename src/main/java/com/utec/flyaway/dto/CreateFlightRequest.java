package com.utec.flyaway.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateFlightRequest(

        @NotBlank(message = "El número de vuelo es obligatorio")
        @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "El número de vuelo solo admite A-Z y 0-9, máximo 6 caracteres")
        String flightNumber,

        @NotBlank(message = "La aerolínea es obligatoria")
        String airline,

        String origin,

        String destination,

        @NotNull(message = "La hora de salida es obligatoria")
        LocalDateTime departureTime,

        @NotNull(message = "La hora de llegada es obligatoria")
        LocalDateTime arrivalTime,

        @NotNull(message = "Los asientos disponibles son obligatorios")
        @Min(value = 1, message = "Los asientos disponibles deben ser mayores a 0")
        Integer availableSeats
) {
}
