package com.example.sidisauthentication.service;

import com.example.sidisauthentication.model.JWT;
import com.example.sidisauthentication.model.LoginRequest;
import com.example.sidisauthentication.model.UserDTO;
import com.example.sidisauthentication.repository.JWTRepository;
import com.example.sidisauthentication.repository.UserRepository;
import com.example.sidisauthentication.utils.AES;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {
    @Value("${sidis.secret}")
    private String secret;
    @Autowired
    private JWTRepository jwtRepository;

    @Autowired
    private UserRepository userRepository;
    public String createJWT(LoginRequest loginRequest){
        String string = generate(loginRequest);
        JWT jwt = new JWT(string);
        jwtRepository.save(jwt);
        return string;
    }

    public String generate(LoginRequest loginRequest){
        UserDTO user = userRepository.findUserDTO(loginRequest.getUsername(), AES.encrypt(loginRequest.getPassword()));
        if (user == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found ");
        }
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("username", loginRequest.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
        return jwtToken;
    }
}
