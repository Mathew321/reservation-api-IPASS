package org.hu.reservation.repository;


import org.hu.reservation.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

    @Query(value = "SELECT t.* FROM tables t LEFT JOIN " +
            "(SELECT table_id FROM reservations WHERE reservation_time >= :reservationTimeStart AND reservation_time <= :reservationTimeEnd) AS r " +
            "ON t.table_id = r.table_id WHERE r.table_id IS NULL",
            nativeQuery = true)
    List<Table> findAvailableTables(@Param("reservationTimeStart") LocalDateTime reservationTimeStart,
                                    @Param("reservationTimeEnd") LocalDateTime reservationTimeEnd);

}
