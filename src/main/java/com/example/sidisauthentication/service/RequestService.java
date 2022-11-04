package com.example.sidisauthentication.service;

import com.example.sidisauthentication.model.JWT;
import com.example.sidisauthentication.model.JWTAPOD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RequestService {

    String baseURL="http://localhost:8088/";
    private HttpURLConnection openConn(String Url) throws IOException {

        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        return connection;
    }
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

    public JWT retriveveJWTFromApi(String jwt) {
        String baseUrl = baseURL+"auth/search"+jwt;
        JWT jwtCheck =null;
        try {
            InputStream responseStream = openConn(baseUrl).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            JWTAPOD apod = mapper.readValue(responseStream, JWTAPOD.class);
            jwtCheck= new JWT(apod);
        } catch (IOException e) {
            System.out.println(e);
        }
        return jwtCheck;
    }
}
