package com.stormpath.examples.controllers;

import com.stormpath.examples.exception.UnauthorizedException;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.lang.Strings;
import com.stormpath.sdk.servlet.account.AccountResolver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

@RestController
public class MicroServiceController {
    @Autowired
    Application application;

    @Autowired
    Client client;

    @Value("#{ @environment['service.remote.uri'] ?: 'http://localhost:8081' }")
    String remoteUri;

    private static final String REMOTE_SERVICE_ENDPOINT = "/microservice";

    @RequestMapping("/protectedEndpoint")
    public String protectedEndpoint(HttpServletRequest req) throws Exception {

        if (!isAuthenticated(req)) {
            throw new UnauthorizedException();
        }

        Account account = AccountResolver.INSTANCE.getAccount(req);

        // create a new JWT with all this information
        String secret = client.getApiKey().getSecret();
        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(account.getHref())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60)))
            .setId(UUID.randomUUID().toString());

        String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8")).compact();

        // make request of other micro-service
        GetMethod method = new GetMethod(remoteUri + REMOTE_SERVICE_ENDPOINT + "?token=" + token);
        HttpClient httpClient = new HttpClient();
        int returnCode = httpClient.executeMethod(method);

        BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while(((line = br.readLine()) != null)) {
            buffer.append(line);
        }

        return buffer.toString();
    }

    @RequestMapping(REMOTE_SERVICE_ENDPOINT)
    public String microservice(@RequestParam String token, HttpServletRequest req) throws Exception {
        if (!Strings.hasText(token)) {
            throw new UnauthorizedException();
        }

        // verify jwt
        Jws<Claims> claims =
            Jwts.parser().setSigningKey(client.getApiKey().getSecret().getBytes("UTF-8")).parseClaimsJws(token);

        String accountHref = claims.getBody().getSubject();

        // This should come from cache if we've done our job
        Account account = client.getResource(accountHref, Account.class);

        return account.getFullName();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public void unauthorized(HttpServletResponse resp) throws Exception {
        resp.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not an authorized.");
    }

    private boolean isAuthenticated(HttpServletRequest req) {
        return AccountResolver.INSTANCE.getAccount(req) != null;
    }
}
