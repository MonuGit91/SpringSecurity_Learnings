package com.app.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserControllers {
	@GetMapping("/auth")
	public String getuser() {
		log.info("Hi!");
		return "authenticated user!";
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("hellouser")
	public String userEndpoint() {
		return "Hello, User!";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("helloadmin")
	public String adminEndpoint() {
		return "Hello, admin!";
	}
}
