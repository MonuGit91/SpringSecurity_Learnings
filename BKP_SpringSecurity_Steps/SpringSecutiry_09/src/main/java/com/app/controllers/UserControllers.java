package com.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.utilities.JwtUtils;
import com.app.utilities.LoginRequest;
import com.app.utilities.LoginResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserControllers {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
		Authentication authentication;
		
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException authenticationException) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
			
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwtToken = jwtUtils.genrateTokenFromUsername(userDetails);
		List<String> roles = userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
		
		LoginResponse response = LoginResponse.builder()
				.username(userDetails.getUsername())
				.jwtToken(jwtToken)
				.roles(roles)
				.build();
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/common")
	public String commonEndpoint() {
		return "Common Endpoint";
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/hellouser")
	public String userEndpoint() {
		return "Hello, User!";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/helloadmin")
	public String adminEndpoint() {
		return "Hello, admin!";
	}
}
