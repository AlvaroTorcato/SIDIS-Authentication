package com.example.sidisauthentication.repository;

import com.example.sidisauthentication.model.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JWTRepository extends JpaRepository<JWT,Integer> {
    @Query("select u from JWT u where u.token = :token")
    boolean search(@Param("token") String jwt);
}
