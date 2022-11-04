package com.example.sidisauthentication.model;

import com.example.sidisauthentication.utils.AES;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jwt")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JWT implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String token;

    public JWT(String token) {
        this.token = token;
    }

    public JWT(JWTAPOD apod) {
        this.id = apod.getId();
        this.token = apod.getToken();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
