package org.hu.reservation.controller;

import org.hu.reservation.dto.Error;
import org.hu.reservation.dto.StaffId;
import org.hu.reservation.entity.StaffInfo;
import org.hu.reservation.service.StaffInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Value("${security.reservation.api-key}")
    private String apiKey;

    @Autowired
    private StaffInfoService service;

    @PostMapping
    public ResponseEntity<?> addNewStaffMember(@RequestBody StaffInfo staffInfo, @RequestHeader(name = "X-Reservation-Api-Key") String apiKeyHeader) {
        try {
            if (!apiKey.equals(apiKeyHeader)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Error("Operation forbidden"));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(new StaffId(service.addStaffMemeber(staffInfo)));
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("Server unavailable", e.getMessage()));
        }
    }


}
