package com.example.messenger.chat.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public List<String> usernames() {
    return List.of("alice", "bob", "ahmed");
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(List<String> usernames) {
    return new InMemoryUserDetailsManager(
        usernames.stream()
            .map(username -> User.withUsername(username)
                .password("{noop}password")
                .roles("USER")
                .build())
            .toList()
    );
  }

  @Bean
  @Order(1)
  public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**") // Only apply this config to /api/**
        .authorizeHttpRequests(authz -> authz
            .anyRequest().authenticated()
        )
        .httpBasic(basic -> {
        }) // Enable HTTP Basic for /api/**
        .csrf(csrf -> csrf.disable()); // (Optional: disable CSRF for APIs)
    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain formLoginSecurity(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
            .anyRequest().authenticated()
        )
//        .formLogin(form -> form.defaultSuccessUrl("/", true))
        .formLogin(form -> form.defaultSuccessUrl("/chat-client.html", true));
    // .formLogin(form -> {});
    //.httpBasic(basic -> {}); // enable HTTP Basic with default settings
    return http.build();
  }

}