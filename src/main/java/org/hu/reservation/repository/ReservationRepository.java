package org.hu.reservation.repository;


import org.hu.reservation.entity.Reservation;
import org.hu.reservation.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Additional query methods can be defined here if needed

    Optional<List<Reservation>> findByReservationToken(String reservationToken);

    List<Reservation> findByReservationTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
