package com.example.demo.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.JWTToken;
import com.example.demo.entity.User;
import com.example.demo.repository.JWTTokenRepository;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
	private final String SIGNING_KEY = "zj3E9KD5FGQh!X7a9Mpn&bTyVw2@8NZL4oCrYq6kJU%Rv#WA1XdPt$EmH*Q\n";
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public AuthService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	public User authenticate(String username, String password) { 
		User user = userRepository.findByUsername(username) .orElseThrow(() -> new RuntimeException("Invalid username or password")); 
		if (!passwordEncoder.matches(password, user.getPassword())) 
		{ 
			throw new RuntimeException("Invalid username or password"); 
		}
		return user; 
		} public String generateToken(User user) { 
			Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
			return Jwts.builder() .setSubject(user.getUsername())
					.claim("role", user.getRole().name()) 
					.setIssuedAt(new Date()) 
					.setExpiration(new Date(System.currentTimeMillis() + 3600000)) 
					.signWith(key, SignatureAlgorithm.HS512) 
					.compact(); 
			}
		public boolean validateToken(String token) {
			try { 
				
				Jwts.parserBuilder()
				.setSigningKey(SIGNING_KEY) 
				.build() 
				.parseClaimsJws(token);
				
				
				Optional<JWTToken> jwtToken = JWTTokenRepository.findByToken(token);
				
				return jwtToken.isPresent() && jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
				} 
			      catch (Exception e)  
		       	{
					return false; 
					}

			} public String extractUsername(String token) { 
				return Jwts.parserBuilder()
				.setSigningKey(SIGNING_KEY) 
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
				}
		}
		
		
	
	 

