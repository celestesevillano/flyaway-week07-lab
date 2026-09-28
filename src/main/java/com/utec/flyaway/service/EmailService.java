package com.utec.flyaway.service;

import com.utec.flyaway.domain.Booking;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Path EMAILS_DIR = Path.of("emails");

    public void sendBookingConfirmation(Booking booking) {
        try {
            Files.createDirectories(EMAILS_DIR);

            String fileName = "flight_booking_email_" + booking.getId() + ".txt";
            Path filePath = EMAILS_DIR.resolve(fileName);

            String content = buildContent(booking);

            try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(content);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo generar el email de confirmación", e);
        }
    }

    private String buildContent(Booking booking) {
        DateTimeFormatter iso = DateTimeFormatter.ISO_DATE_TIME;

        return """
                Confirmación de reserva - Fly Away Travel
                ==========================================

                Pasajero: %s %s
                Número de vuelo: %s
                Fecha de salida: %s
                Fecha de llegada: %s
                Fecha de reserva: %s
                """.formatted(
                booking.getCustomerFirstName(),
                booking.getCustomerLastName(),
                booking.getFlight().getFlightNumber(),
                booking.getFlight().getDepartureTime().format(iso),
                booking.getFlight().getArrivalTime().format(iso),
                booking.getBookingDate().format(iso)
        );
    }
}
