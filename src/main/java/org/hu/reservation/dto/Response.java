package org.hu.reservation.dto;

public class Response<L, R> {

    L error;

    R response;

    public Response(L error, R response) {
        this.error = error;
        this.response = response;
    }

    public L getError() {
        return error;
    }

    public void setError(L error) {
        this.error = error;
    }

    public R getResponse() {
        return response;
    }

    public void setResponse(R response) {
        this.response = response;
    }
}
