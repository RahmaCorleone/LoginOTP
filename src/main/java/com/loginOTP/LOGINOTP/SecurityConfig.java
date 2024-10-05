package com.loginOTP.LOGINOTP;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/verify-otp", "/login", "/h2-console/**").permitAll()  // Permit all access to these endpoints
                        .anyRequest().authenticated()  // Secure all other endpoints
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Redirects to this custom login page
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .csrf(csrf -> csrf.disable())  // Disable CSRF for development/testing (enable it in production)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));  // For H2 console access

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
