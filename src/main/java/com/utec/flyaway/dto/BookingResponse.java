package com.utec.flyaway.dto;

import com.utec.flyaway.domain.Booking;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long flightId,
        String flightNumber,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        LocalDateTime bookingDate
) {
    public static BookingResponse fromEntity(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getFlight().getId(),
                booking.getFlight().getFlightNumber(),
                booking.getCustomer().getId(),
                booking.getCustomerFirstName(),
                booking.getCustomerLastName(),
                booking.getBookingDate()
        );
    }
}
