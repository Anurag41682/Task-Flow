package com.anurag.task_flow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.task_flow.dto.request.LoginRequest;
import com.anurag.task_flow.dto.request.SetPasswordRequest;
import com.anurag.task_flow.dto.request.SignupRequest;
import com.anurag.task_flow.dto.response.JwtResponse;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.security.AuthResult;
import com.anurag.task_flow.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  // working
  @PostMapping("/signup")
  ResponseEntity<Void> Signup(@RequestBody SignupRequest request) {

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setEnabled(true);
    authService.signup(user);
    return ResponseEntity.ok(null);
  }

  // working
  @PostMapping("/login")
  ResponseEntity<JwtResponse> Login(@RequestBody LoginRequest request) {

    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    AuthResult res = authService.login(user);
    JwtResponse response = new JwtResponse();
    response.setEmail(res.getEmail());
    response.setToken(res.getJwtToken());
    response.setRole(res.getRole());
    return ResponseEntity.ok(response);
  }

  // working
  @PostMapping("/set-password")
  ResponseEntity<String> setPassword(@RequestParam String token, @RequestBody SetPasswordRequest request) {
    authService.setPassword(token, request);
    return ResponseEntity.ok("Password set successfully");
  }

}
