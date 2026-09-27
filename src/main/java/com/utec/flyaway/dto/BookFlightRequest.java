package com.utec.flyaway.dto;

import jakarta.validation.constraints.NotNull;

public record BookFlightRequest(
        @NotNull(message = "El flightId es obligatorio")
        Long flightId
) {
}
