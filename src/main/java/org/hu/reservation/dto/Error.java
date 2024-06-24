package org.hu.reservation.dto;

public class Error {

    private String error;

    // Constructors
    public Error() {
    }

    public Error(String error) {
        this.error = error;
    }

    // Getter and Setter
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
