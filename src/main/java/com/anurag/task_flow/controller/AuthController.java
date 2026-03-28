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
import com.anurag.task_flow.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  ResponseEntity<JwtResponse> Login(@RequestBody LoginRequest request) {
    JwtResponse res = authService.login(request);
    return ResponseEntity.ok(res);
  }

  @PostMapping("/set-password")
  ResponseEntity<String> setPassword(@RequestParam String token, @Valid @RequestBody SetPasswordRequest request) {
    authService.setPassword(token, request);
    return ResponseEntity.ok("Password set successfully");
  }

}
