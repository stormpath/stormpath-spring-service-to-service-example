package com.stormpath.examples.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stormpath.examples.model.AccountsResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CommunicationService {
    public AccountsResponse doRemoteRequest(String url, String token) throws Exception {
        // make request of other micro-service
        GetMethod method = new GetMethod(url + "?token=" + token);
        HttpClient httpClient = new HttpClient();
        int returnCode = httpClient.executeMethod(method);

        BufferedReader br = new BufferedReader(
            new InputStreamReader(method.getResponseBodyAsStream())
        );
        StringBuffer buffer = new StringBuffer();
        String line;
        while(((line = br.readLine()) != null)) {
            buffer.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(buffer.toString(), AccountsResponse.class);
    }
}
