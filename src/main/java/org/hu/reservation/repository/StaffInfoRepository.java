package org.hu.reservation.repository;

import org.hu.reservation.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffInfoRepository extends JpaRepository<StaffInfo, Integer> {
    Optional<StaffInfo> findByName(String username);
}
