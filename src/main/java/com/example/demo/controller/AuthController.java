package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Service.AuthService;
import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try 
	{ 
		User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
		 String token = authService.generateToken(user);
		  Cookie cookie = new Cookie("authToken", token); 
		  cookie.setHttpOnly(true);
		  cookie.setSecure(false); 
		  cookie.setPath("/"); cookie.setMaxAge(3600);
		   response.addCookie(cookie); 
		    Map<String, String> responseBody = new HashMap<>();
		    responseBody.put("message", "Login successful"); 
		    responseBody.put("role", user.getRole().name());
		    return ResponseEntity.ok(responseBody); 
		    } 
	catch (RuntimeException e) 
	{
	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
     } 
	}
	@PostMapping("/logout") 
	public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
		try
		{ 
			authService.logout(response);
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("message", "Logout successful"); 
			return ResponseEntity.ok(responseBody); 
			}
		  catch (RuntimeException e)
		{
		Map<String, String> errorResponse = new HashMap<>(); 
		errorResponse.put("message", "Logout failed");

	return ResponseEntity.status(500).body(errorResponse);
	}
}
}
	
	

