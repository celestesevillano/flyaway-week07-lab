package com.utec.flyaway.repository;

import com.utec.flyaway.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumber(String flightNumber);

    @Query("""
            SELECT f FROM Flight f
            WHERE (:flightNumber IS NULL OR UPPER(f.flightNumber) LIKE UPPER(CONCAT('%', :flightNumber, '%')))
              AND (:airline IS NULL OR UPPER(f.airline) LIKE UPPER(CONCAT('%', :airline, '%')))
              AND (:from IS NULL OR f.departureTime >= :from)
              AND (:to IS NULL OR f.departureTime <= :to)
            """)
    List<Flight> search(
            @Param("flightNumber") String flightNumber,
            @Param("airline") String airline,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
