package org.hu.reservation.service;

import org.hu.reservation.entity.Reservation;
import org.hu.reservation.repository.ReservationRepository;
import org.hu.reservation.repository.TableRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteReservation() {
        String reservationToken = "test-token";
        Reservation reservation = new Reservation();
        List<Reservation> reservationList = List.of(reservation);

        when(reservationRepository.findByReservationToken(reservationToken)).thenReturn(Optional.of(reservationList));

        reservationService.deleteReservation(reservationToken);

        verify(reservationRepository, times(1)).deleteAll(reservationList);
    }

    @Test
    void testGetReservationsByDate() {
        LocalDate reservationDate = LocalDate.of(2024, 9, 25);
        LocalDateTime startOfDay = reservationDate.atStartOfDay();
        LocalDateTime endOfDay = reservationDate.atTime(LocalTime.MAX);
        List<Reservation> reservations = Collections.singletonList(new Reservation());
        when(reservationRepository.findByReservationTimeBetween(startOfDay, endOfDay)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationsByDate(reservationDate);

        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findByReservationTimeBetween(startOfDay, endOfDay);
    }

    @Test
    void testGetAvailableTables() {
        LocalDate reservationDate = LocalDate.of(2024, 9, 25);
        LocalDateTime morningStart = reservationDate.atStartOfDay();
        LocalDateTime morningEnd = reservationDate.atTime(LocalTime.of(15, 59, 59));
        LocalDateTime eveningStart = reservationDate.atTime(LocalTime.of(16, 0));
        LocalDateTime eveningEnd = reservationDate.atTime(LocalTime.MAX);

        when(reservationRepository.findByReservationTimeBetween(morningStart, morningEnd)).thenReturn(Collections.emptyList());
        when(reservationRepository.findByReservationTimeBetween(eveningStart, eveningEnd)).thenReturn(Collections.emptyList());
        when(tableRepository.count()).thenReturn(10L);

        int freeTablesMorning = reservationService.getAvailableTables(reservationDate).getFreeTablesMorning();
        int freeTablesEvening = reservationService.getAvailableTables(reservationDate).getFreeTablesEvening();

        assertEquals(10, freeTablesMorning);
        assertEquals(10, freeTablesEvening);
    }
}
