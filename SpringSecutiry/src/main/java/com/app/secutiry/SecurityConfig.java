package com.app.secutiry;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// command + shift + T = SecurityFilterChainConfiguration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // ✅ Every request must be authenticated — no public endpointsO
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
        );
        
        /*
         ✅ Make the application STATELESS (recommended for REST APIs)
         - Spring Security will NOT create or use HttpSession
         - No JSESSIONID cookie will be sent to the browser
         - Server will NOT remember logged-in user between requests
         - Each request must contain authentication info again
        */
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        
        /*
         ✅ Enable HTTP Basic Authentication
         - Browser/clients send credentials in Authorization header:
               Authorization: Basic base64(username:password)
         - Good for testing / internal APIs
         - Stateless by nature because credentials are sent every time
        */
        http.httpBasic(withDefaults());
        
        /*
         🚫 formLogin() intentionally disabled
         - formLogin creates login page & uses HttpSession to store authentication
         - Session login = STATEFUL, opposite of stateless API
         - Should NOT be used when sessionCreationPolicy = STATELESS
        */
        // http.formLogin(withDefaults());

        return http.build();
    }
}
/*
 * 
 * 
 * 
 */
