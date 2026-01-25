package com.anurag.task_flow.security;

import com.anurag.task_flow.entity.Role;

import lombok.Data;

@Data
public class AuthResult {
  private Long userId;
  private String email;
  private Role role;
  private String jwtToken;
}
