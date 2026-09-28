package com.utec.flyaway.service;

import com.utec.flyaway.domain.Booking;
import com.utec.flyaway.domain.Flight;
import com.utec.flyaway.domain.User;
import com.utec.flyaway.dto.BookFlightRequest;
import com.utec.flyaway.dto.BookingResponse;
import com.utec.flyaway.exception.BadRequestException;
import com.utec.flyaway.exception.ConflictException;
import com.utec.flyaway.exception.ResourceNotFoundException;
import com.utec.flyaway.repository.BookingRepository;
import com.utec.flyaway.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final EmailService emailService;

    @Transactional
    public BookingResponse book(BookFlightRequest request, User customer) {
        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado"));

        LocalDateTime now = LocalDateTime.now();

        if (!flight.getDepartureTime().isAfter(now)) {
            throw new BadRequestException("No se puede reservar un vuelo que ya partió o está en tránsito");
        }

        if (flight.getAvailableSeats() <= 0) {
            throw new ConflictException("No hay asientos disponibles para este vuelo");
        }

        List<Booking> existingBookings = bookingRepository.findByCustomerId(customer.getId());
        boolean hasConflict = existingBookings.stream().anyMatch(b -> overlaps(b.getFlight(), flight));
        if (hasConflict) {
            throw new ConflictException("Ya tienes una reserva con un horario que se superpone con este vuelo");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = Booking.builder()
                .flight(flight)
                .customer(customer)
                .customerFirstName(customer.getFirstName())
                .customerLastName(customer.getLastName())
                .bookingDate(now)
                .build();

        Booking saved = bookingRepository.save(booking);

        emailService.sendBookingConfirmation(saved);

        return BookingResponse.fromEntity(saved);
    }

    public BookingResponse findById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        return BookingResponse.fromEntity(booking);
    }

    private boolean overlaps(Flight a, Flight b) {
        return a.getDepartureTime().isBefore(b.getArrivalTime())
                && b.getDepartureTime().isBefore(a.getArrivalTime());
    }
}
