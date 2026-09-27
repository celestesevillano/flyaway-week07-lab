package com.utec.flyaway.dto;

import com.utec.flyaway.domain.Flight;

import java.time.LocalDateTime;

public record FlightResponse(
        Long id,
        String flightNumber,
        String airline,
        String origin,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Integer availableSeats
) {
    public static FlightResponse fromEntity(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAvailableSeats()
        );
    }
}
