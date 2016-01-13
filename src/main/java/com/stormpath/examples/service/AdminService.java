package com.stormpath.examples.service;

import com.stormpath.examples.model.AccountsResponseBuilder;
import com.stormpath.examples.model.AccountsResponse;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Value("#{ @environment['stormpath.admin.group.href'] }")
    String adminGroupHref;

    @Autowired
    Application application;

    public boolean isAdmin(Account account) {
        return account.isMemberOfGroup(adminGroupHref);
    }

    public AccountsResponse buildAccountsResponse(Account account) {
        AccountsResponseBuilder accountsResponseBuilder = AccountsResponseBuilder.newInstance();
        if (isAdmin(account)) {
            List<String> emails = new ArrayList<String>();
            for (Account acc : application.getAccounts()) {
                emails.add(acc.getEmail());
            }
            accountsResponseBuilder.emails(emails).status(AccountsResponse.STATUS.OK).message("Success!");
        } else {
            accountsResponseBuilder.status(AccountsResponse.STATUS.ERROR).message("You must be an admin!");
        }

        return accountsResponseBuilder.build();
    }
}
