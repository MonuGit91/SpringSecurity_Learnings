package com.app.utilities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
	private String jwtToken;
	private String username;
	private List<String> roles;
}
