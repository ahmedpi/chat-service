package com.example.messenger.chat.adapters.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserInfoController {
  private final InMemoryUserDetailsManager userDetailsService;
  private final List<String> usernames;

  @GetMapping("/whoami")
  public Map<String, String> whoami(@AuthenticationPrincipal UserDetails user) {
    return Map.of("username", user.getUsername());
  }

  @GetMapping("/users")
  public List<String> getUsers(@AuthenticationPrincipal UserDetails currentUser) {
    // For InMemoryUserDetailsManager, get all users except the current one
    return usernames.stream()
        .filter(username -> !username.equals(currentUser.getUsername()))
        .toList();
  }
}
