package org.hu.reservation.dto;

public class AvailableTablesResponse {

    private int freeTablesMorning;
    private int freeTablesEvening;

    // Constructors
    public AvailableTablesResponse() {
    }

    public AvailableTablesResponse(int freeTablesMorning, int freeTablesEvening) {
        this.freeTablesMorning = freeTablesMorning;
        this.freeTablesEvening = freeTablesEvening;
    }

    // Getters and Setters
    public int getFreeTablesMorning() {
        return freeTablesMorning;
    }

    public void setFreeTablesMorning(int freeTablesMorning) {
        this.freeTablesMorning = freeTablesMorning;
    }

    public int getFreeTablesEvening() {
        return freeTablesEvening;
    }

    public void setFreeTablesEvening(int freeTablesEvening) {
        this.freeTablesEvening = freeTablesEvening;
    }
}
