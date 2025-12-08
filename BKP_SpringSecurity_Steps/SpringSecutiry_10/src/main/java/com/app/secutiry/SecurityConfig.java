package com.app.secutiry;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.utilities.AuthEntryPointJWT;
import com.app.utilities.AuthTokenFilter;

//command + shift + T = SecurityFilterChainConfiguration

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	DataSource dataSource;
	@Autowired
	private AuthEntryPointJWT unauthorizedHandler;

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		// ✅ Every request must be authenticated — no public endpoints
//		http.authorizeHttpRequests(auth -> auth.requestMatchers("/h2-console", "/h2-console/**").permitAll()
//				.requestMatchers("/api/login").permitAll().anyRequest().authenticated());
		
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/login").permitAll().anyRequest().authenticated());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
		http.csrf(csrf -> csrf.disable());

		http.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}

	@Bean
	public AuthTokenFilter authJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		// We only return the mechanism (JdbcUserDetailsManager) for looking up users.
		// We do NOT try to insert data here, as the database tables might not exist
		// yet.
		return new JdbcUserDetailsManager(dataSource);
	}

	// 2. The Bean that inserts the initial data (runs AFTER the DB schema is
	// created)
	@Bean
	public CommandLineRunner initData(UserDetailsService userDetailsService) {
		return args -> {
			// Cast the UserDetailsService back to its concrete implementation (or inject
			// JdbcUserDetailsManager directly)
			JdbcUserDetailsManager jdbcUserDetailsManager = (JdbcUserDetailsManager) userDetailsService;

			// Only proceed if the user doesn't already exist to prevent re-creation on
			// every startup
			if (!jdbcUserDetailsManager.userExists("admin")) {
				UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin"))
						.roles("ADMIN").build();
				UserDetails user1 = User.withUsername("user1").password(passwordEncoder().encode("user1"))
						.roles("USER").build();

				jdbcUserDetailsManager.createUser(admin);
				jdbcUserDetailsManager.createUser(user1);
			}
		};
	}

	/*
	 * //why CommandLineRunner aproach works while below aborach gives and error
	 * saying: Error starting Tomcat context. Exception:
	 * org.springframework.beans.factory.UnsatisfiedDependencyException. Message:
	 * Error creating bean with name 'authJwtTokenFilter': Unsatisfied dependency
	 * expressed through field 'userDetailsService': Error creating bean with name
	 * 'userDetailsService' defined in class path resource
	 * [com/app/secutiry/SecurityConfig.class]: Failed to instantiate
	 * [org.springframework.security.core.userdetails.UserDetailsService]: Factory
	 * method 'userDetailsService' threw exception with message:
	 * PreparedStatementCallback; bad SQL grammar [insert into users (username,
	 * password, enabled) values (?,?,?)]
	 * 
	 * Answer: The direct @Bean approach fails because it attempts to insert data
	 * into the USERS table before Spring Security's initialization scripts have
	 * automatically created that table. CommandLineRunner is guaranteed to execute
	 * after the application context is fully loaded and the database schema is
	 * ready, ensuring the USERS table exists for data insertion.
	 */

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("AdminPass")).roles("ADMIN")
//				.build();
//		UserDetails user1 = User.withUsername("user1").password(passwordEncoder().encode("User1Pass")).roles("USER")
//				.build();
//		UserDetails user2 = User.withUsername("user2").password(passwordEncoder().encode("User2Pass")).roles("USER")
//				.build();
//
//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//		jdbcUserDetailsManager.createUser(admin);
//		jdbcUserDetailsManager.createUser(user1);
//		jdbcUserDetailsManager.createUser(user2);
//		return jdbcUserDetailsManager;
//	}

}
