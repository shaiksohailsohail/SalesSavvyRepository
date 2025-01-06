package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.JWTToken;

public interface JWTTokenRepositary extends JpaRepository<JWTToken, Integer> {

}
