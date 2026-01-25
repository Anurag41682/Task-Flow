package com.anurag.task_flow.dto.response;

import com.anurag.task_flow.entity.Role;

import lombok.Data;

@Data
public class JwtResponse {
  private String token;
  private String email;
  private Role role;
}
