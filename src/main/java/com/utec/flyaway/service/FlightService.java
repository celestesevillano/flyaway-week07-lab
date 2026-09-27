package com.utec.flyaway.service;

import com.utec.flyaway.domain.Flight;
import com.utec.flyaway.dto.CreateFlightRequest;
import com.utec.flyaway.dto.FlightResponse;
import com.utec.flyaway.exception.BadRequestException;
import com.utec.flyaway.exception.ConflictException;
import com.utec.flyaway.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    @Transactional
    public FlightResponse create(CreateFlightRequest request) {
        if (!request.departureTime().isBefore(request.arrivalTime())) {
            throw new BadRequestException("La hora de salida debe ser anterior a la hora de llegada");
        }

        if (flightRepository.existsByFlightNumber(request.flightNumber())) {
            throw new ConflictException("Ya existe un vuelo con ese número de vuelo");
        }

        Flight flight = Flight.builder()
                .flightNumber(request.flightNumber())
                .airline(request.airline())
                .origin(request.origin())
                .destination(request.destination())
                .departureTime(request.departureTime())
                .arrivalTime(request.arrivalTime())
                .availableSeats(request.availableSeats())
                .build();

        Flight saved = flightRepository.save(flight);
        return FlightResponse.fromEntity(saved);
    }

    public List<FlightResponse> search(String flightNumber, String airline, LocalDateTime from, LocalDateTime to) {
        return flightRepository.search(
                        blankToNull(flightNumber),
                        blankToNull(airline),
                        from == null ? LocalDateTime.of(1970, 1, 1, 0, 0) : from,
                        to == null ? LocalDateTime.of(9999, 12, 31, 23, 59, 59) : to
                ).stream()
                .map(FlightResponse::fromEntity)
                .toList();
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
