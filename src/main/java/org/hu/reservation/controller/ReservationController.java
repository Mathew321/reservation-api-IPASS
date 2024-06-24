package org.hu.reservation.controller;

import jakarta.validation.Valid;
import org.hu.reservation.dto.Error;
import org.hu.reservation.dto.ReservationRequest;
import org.hu.reservation.dto.Response;
import org.hu.reservation.dto.Token;
import org.hu.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@Validated
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/make")
    public ResponseEntity<?> makeReservation(@RequestBody @Valid ReservationRequest reservationRequest) {
        try {
            Response<Error, Token> response = reservationService.makeReservation(reservationRequest);
            if (response.getError() != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getError());
            } else
                return ResponseEntity.status(HttpStatus.CREATED).body(response.getResponse());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("Server unavailable"));
        }
    }
}
