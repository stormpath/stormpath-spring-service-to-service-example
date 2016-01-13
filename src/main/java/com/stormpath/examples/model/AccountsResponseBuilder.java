package com.stormpath.examples.model;

import org.springframework.util.Assert;

import java.util.List;

public class AccountsResponseBuilder {
    private String message;
    private AccountsResponse.STATUS status;
    private List<String> emails;

    public static AccountsResponseBuilder newInstance() {
        return new AccountsResponseBuilder();
    }

    public AccountsResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public AccountsResponseBuilder status(AccountsResponse.STATUS status) {
        this.status = status;
        return this;
    }

    public AccountsResponseBuilder emails(List<String> emails) {
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
