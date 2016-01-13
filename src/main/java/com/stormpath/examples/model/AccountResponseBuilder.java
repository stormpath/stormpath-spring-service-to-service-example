package com.stormpath.examples.model;

import org.springframework.util.Assert;

import java.util.List;

public class AccountResponseBuilder {
    private String message;
    private AccountsResponse.STATUS status;
    private List<String> emails;

    public static AccountResponseBuilder newInstance() {
        return new AccountResponseBuilder();
    }

    public AccountResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public AccountResponseBuilder status(AccountsResponse.STATUS status) {
        this.status = status;
        return this;
    }

    public AccountResponseBuilder emails(List<String> emails) {
        this.emails = emails;
        return this;
    }

    public AccountsResponse build() {
        Assert.notNull(status, "status must be set.");
        Assert.notNull(message, "message must be set.");

        AccountsResponse accountResponse = new AccountsResponse();
        accountResponse.setEmails(emails);
        accountResponse.setMessage(message);
        accountResponse.setStatus(status);

        return accountResponse;
    }
}
