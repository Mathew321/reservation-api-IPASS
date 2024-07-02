package org.hu.reservation.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ReservationRequest {

    @NotBlank(message = "Date is required")
    private LocalDateTime date;

    @NotNull(message = "Persons is required")
    private Integer persons;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    private String phone;

    @NotBlank(message = "Name is required")
    private String name;

    public ReservationRequest() {
    }

    public ReservationRequest(LocalDateTime date, Integer persons, String email, String phone, String name) {
        this.date = date;
        this.persons = persons;
        this.email = email;
        this.phone = phone;
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getPersons() {
        return persons;
    }

    public void setPersons(Integer persons) {
        this.persons = persons;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
