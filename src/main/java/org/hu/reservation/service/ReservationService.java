package org.hu.reservation.service;

import org.hu.reservation.dto.*;
import org.hu.reservation.dto.Error;
import org.hu.reservation.entity.Reservation;
import org.hu.reservation.entity.Table;
import org.hu.reservation.repository.ReservationRepository;
import org.hu.reservation.repository.TableRepository;
import org.hu.reservation.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    // @Autowired
    // private EmailService emailService;

    public Response<Error, Token> makeReservation(ReservationRequest reservationRequest) {
        List<Table> tables = checkAvailability(reservationRequest);

        if (tables.isEmpty()) {
            return new Response<>(new Error("Booking is not available. Not enough seats."), null);
        }

        int sum = tables.stream().mapToInt(Table::getSeatingCapacity).sum();
        if (sum < reservationRequest.getPersons())
            return new Response<>(new Error("Booking is not available. Not enough seats."), null);

        int requestedSeats = reservationRequest.getPersons();
        String token = UUID.randomUUID().toString();

        for (int i = 0; i < tables.size() && requestedSeats > 0; i++) {
            Table table = tables.get(i);
            Reservation reservation = new Reservation();
            reservation.setEmail(reservationRequest.getEmail());
            reservation.setCustomerName(reservationRequest.getName());
            reservation.setPhone(reservationRequest.getPhone());
            reservation.setNumberOfPersons(reservationRequest.getPersons());
            reservation.setReservationTime(reservationRequest.getDate());
            reservation.setReservationToken(token);
            reservation.setTable(table);

            reservationRepository.save(reservation);
            requestedSeats -= table.getSeatingCapacity();
        }
        return new Response<>(null, new Token(token));
    }

    private List<Table> checkAvailability(ReservationRequest reservationRequest) {
        LocalDateTime morningOrEvening = Utils.toMorningOrEvening(reservationRequest.getDate());
        return tableRepository.findAvailableTables(morningOrEvening, morningOrEvening.plusHours(6));
    }

    public Response<Error, Void> deleteReservation(String reservationToken) {
        Optional<List<Reservation>> reservations = reservationRepository.findByReservationToken(reservationToken);
        if (reservations.isPresent()) {
            reservationRepository.deleteAll(reservations.get());
            return new Response<>(null, null);
        }
        return new Response<>(new Error("Reservation not found with the provided token"), null);
    }

    public List<Reservation> getReservationsByDate(LocalDate reservationDate) {
        LocalDateTime startOfDay = reservationDate.atStartOfDay();
        LocalDateTime endOfDay = reservationDate.atTime(LocalTime.MAX);
        return reservationRepository.findByReservationTimeBetween(startOfDay, endOfDay);
    }

    private int countAvailableTables(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Reservation> reservations = reservationRepository.findByReservationTimeBetween(startDateTime, endDateTime);
        long reservedTableCount = reservations.stream().map(Reservation::getTable).distinct().count();
        long totalTableCount = tableRepository.count();
        return (int) (totalTableCount - reservedTableCount);
    }

    public AvailableTablesResponse getAvailableTables(LocalDate reservationDate) {
        LocalDateTime morningStart = reservationDate.atStartOfDay();
        LocalDateTime morningEnd = reservationDate.atTime(LocalTime.of(15, 59, 59));

        LocalDateTime eveningStart = reservationDate.atTime(LocalTime.of(16, 0));
        LocalDateTime eveningEnd = reservationDate.atTime(LocalTime.MAX);

        int freeTablesMorning = countAvailableTables(morningStart, morningEnd);
        int freeTablesEvening = countAvailableTables(eveningStart, eveningEnd);

        return new AvailableTablesResponse(freeTablesMorning, freeTablesEvening);
    }

}
