package org.hu.reservation.service;

import org.hu.reservation.dto.Error;
import org.hu.reservation.dto.ReservationRequest;
import org.hu.reservation.dto.Response;
import org.hu.reservation.dto.Token;
import org.hu.reservation.entity.Reservation;
import org.hu.reservation.entity.Table;
import org.hu.reservation.repository.ReservationRepository;
import org.hu.reservation.repository.TableRepository;
import org.hu.reservation.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
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

        if (tables.isEmpty()){
            return new Response<>(new Error("Booking is not available. Not enough seats."), null);
        }

        int sum = tables.stream().mapToInt(Table::getSeatingCapacity).sum();
        if (sum<reservationRequest.getPersons())
            return new Response<>(new Error("Booking is not available. Not enough seats."), null);

        int requestedSeats = reservationRequest.getPersons();
        String token = UUID.randomUUID().toString();

        for (int i = 0; i < tables.size() && requestedSeats>0; i++) {
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
}
