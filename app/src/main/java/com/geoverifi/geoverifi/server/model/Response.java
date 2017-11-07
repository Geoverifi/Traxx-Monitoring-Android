package com.geoverifi.geoverifi.server.model;

/**
 * Created by chriz on 8/31/2017.
 */

public class Response {
    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
