package com.example.sidisauthentication.service;

import com.example.sidisauthentication.model.*;
import com.example.sidisauthentication.repository.JWTRepository;
import com.example.sidisauthentication.repository.UserRepository;
import com.example.sidisauthentication.utils.AES;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
    @Value("${sidis.secret}")
    private String secret;

    @Value("${sidis.password}")
    private String passwordSecret;

    @Autowired
    private JWTRepository jwtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestService service;
    public String createJWT(LoginRequest loginRequest){
        String string = generate(loginRequest);
        JWT jwt = new JWT(string);
        jwtRepository.save(jwt);
        return string;
    }

    public String generate(LoginRequest loginRequest){
        UserDTO user = userRepository.findUserDTO(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null){
            //try {
            Map<String, String> map;
            map = new HashMap<>();
            map.put("username", loginRequest.getUsername());
            map.put("password", loginRequest.getPassword());
            JSONObject jo = new JSONObject(map);
            System.out.println(jo.toString());
                String str=service.post("http://localhost:8088/auth/signin", jo.toString());/*"{" +
                        "username: "+loginRequest.getUsername()+", " +
                        "password: "+loginRequest.getPassword()+
                        "}");*/
                System.out.println(str);
                return str;
            //}
            //catch (){
                //throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found ");
            //}



        }
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("username", loginRequest.getUsername())
                .claim("id", user.getId())
                .claim("roles", user.getRoles().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(120l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
        return jwtToken;
    }

    public UserDetailsDTO searchForUser(String jwt) {
        JWT find = jwtRepository.search(jwt);
        if (find == null){
            find = service.retriveveJWTFromApi(jwt);
            if (find == null){
                throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "JWT Not Found ");
            }
        }
        Claims claims = Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(jwt).getBody();
        UserDetailsDTO user = new UserDetailsDTO(
                claims.get("id",Integer.class),
                claims.get("username", String.class),
                claims.get("roles",String.class));
        Date expTime = claims.get("exp", Date.class);
        Instant now = Instant.now();
        Date date = Date.from(now);
        if (expTime.after(date)){
           new ResponseStatusException(HttpStatus.FORBIDDEN, "Expired token ");
        }
        return user;

    }

    public UserDetailsDTO searchForUserInternal(String jwt) {
        JWT find = jwtRepository.search(jwt);
        if (find == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "JWT Not Found ");
        }
        Claims claims = Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(jwt).getBody();
        UserDetailsDTO user = new UserDetailsDTO(
                claims.get("id",Integer.class),
                claims.get("username", String.class),
                claims.get("roles",String.class));
        Date expTime = claims.get("exp", Date.class);
        Instant now = Instant.now();
        Date date = Date.from(now);
        if (expTime.after(date)){
            new ResponseStatusException(HttpStatus.FORBIDDEN, "Expired token ");
        }
        return user;

    }
}
