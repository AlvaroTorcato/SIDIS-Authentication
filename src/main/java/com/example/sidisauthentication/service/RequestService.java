package com.example.sidisauthentication.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class RequestService {


    public String post(String completeUrl, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(completeUrl);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);
            //return httpClient.execute(httpPost).toString();
            // Getting the response body.
            String responseBody = EntityUtils.toString(httpClient.execute(httpPost).getEntity());
            //System.out.println(responseBody);
            return responseBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
