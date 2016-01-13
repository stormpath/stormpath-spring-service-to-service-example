package com.stormpath.examples.model;

import java.io.Serializable;
import java.util.List;

public class AccountsResponse implements Serializable {

    public enum STATUS {
        OK, ERROR
    }

    List<String> emails;
    STATUS status;
    String message;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
