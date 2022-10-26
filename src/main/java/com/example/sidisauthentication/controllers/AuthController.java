package com.example.sidisauthentication.controllers;

import com.example.sidisauthentication.model.LoginRequest;

import com.example.sidisauthentication.model.UserDTO;
import com.example.sidisauthentication.service.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints for authenticate the user")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    JWTService service;

    @Operation(summary = "Signin the user")
    @PostMapping("/signin")
    public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return service.createJWT(loginRequest);
    }

    @Operation(summary = "Get info about JWT token")
    @GetMapping("/search/{jwt}")
    public UserDTO searchForUserUsingJWT(@Param("jwt") String jwt){
        return service.searchForUser(jwt);
    }

}
