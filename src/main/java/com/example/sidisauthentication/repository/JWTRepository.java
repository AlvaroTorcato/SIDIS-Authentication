package com.example.sidisauthentication.repository;

import com.example.sidisauthentication.model.JWT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTRepository extends JpaRepository<JWT,Integer> {
}
