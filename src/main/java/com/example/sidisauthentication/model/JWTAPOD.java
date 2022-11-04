package com.example.sidisauthentication.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTAPOD {
    private int id;
    private String token;

    public JWTAPOD(@JsonProperty("id") int id,
                   @JsonProperty("token") String token) {
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
