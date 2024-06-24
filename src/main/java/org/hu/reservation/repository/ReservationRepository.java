package org.hu.reservation.repository;


import org.hu.reservation.entity.Reservation;
import org.hu.reservation.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Additional query methods can be defined here if needed
}
