package com.utec.flyaway.controller;

import com.utec.flyaway.domain.User;
import com.utec.flyaway.dto.BookFlightRequest;
import com.utec.flyaway.dto.BookingResponse;
import com.utec.flyaway.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponse> book(
            @Valid @RequestBody BookFlightRequest request,
            @AuthenticationPrincipal User customer
    ) {
        BookingResponse response = bookingService.book(request, customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Nota: el README lo escribe como "GET /flight/book/{id}" (singular) — se respeta tal cual.
    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }
}
