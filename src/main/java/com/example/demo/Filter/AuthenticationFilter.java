package com.example.demo.Filter;

import java.io.IOException;
import java.util.Optional;



import com.example.demo.Service.AuthService;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {	
	AuthService authService;
	UserRepository userRepository;
	
	
	public AuthenticationFilter(AuthService authService, UserRepository userRepository) {
		super();
		this.authService = authService;
		this.userRepository = userRepository;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	HttpServletRequest httpRequest = (HttpServletRequest) request;
	HttpServletResponse httpResponse = (HttpServletResponse) response;	
		
	if(httpRequest.getRequestURI().equals("/api/users/register") ||
	httpRequest.getRequestURI().equals("/api/auth/login")) {
		chain.doFilter(request, response);
		return;
	}
	if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return;
    }
	
	String token = getAuthTokenFromCookies(httpRequest);
	if(token == null || ! authService.validateToken(token)) {
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.getWriter().write("Unauthorize: Invalid or missing Token");
		return;
	}
	String username = authService.extractUsername(token);
	Optional<User> userOptional = userRepository.findByUsername(username);
	if(userOptional.isEmpty()) {
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.getWriter().write("Unauthorize: User not found");
		return;
	}
	httpRequest.setAttribute("authenticatedUser", userOptional.get());
	chain.doFilter(request, response);
	
	}
	private String getAuthTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies!= null) {
			return Arrays.stream(cookies)
			   .filter(cookie -> "authToken".equals(cookie.getname()))
			   .map(Cookie::getvalue)
			   .findfirst()
			   .orElse(null);		
		}
		return null;
	}
}
