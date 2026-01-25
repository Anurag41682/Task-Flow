package com.anurag.task_flow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.task_flow.dto.request.UserRequest;
import com.anurag.task_flow.dto.response.UserResponse;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@EnableMethodSecurity
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  private UserResponse mapToUserResponse(User user) {
    UserResponse response = new UserResponse();
    response.setEmail(user.getEmail());
    response.setName(user.getName());
    response.setId(user.getId());
    return response;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
    UserResponse savedUser = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponse>> getAllUser() {
    List<UserResponse> response = userService.getAllUsers();
    return ResponseEntity.ok(response);
  }

  // working
  @GetMapping("/{id}")
  // only allows the admin and the current user to use this API
  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.getUserId()")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(mapToUserResponse(user));
  }

}
