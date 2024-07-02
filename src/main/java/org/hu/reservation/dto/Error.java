package org.hu.reservation.dto;

public class Error {

    private String error;
    private String detailedMessage = "";

    // Constructors
    public Error() {
    }

    public Error(String error) {
        this.error = error;
    }

    public Error(String error, String detailedMessage) {
        this.error = error;
        this.detailedMessage = detailedMessage;
    }

    // Getter and Setter
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }
}
