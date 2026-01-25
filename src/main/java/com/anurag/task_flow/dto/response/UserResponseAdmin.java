package com.anurag.task_flow.dto.response;

import lombok.Data;

@Data
public class UserResponseAdmin {
  private Long id;
  private Boolean enabled;
  private String name;
  private String email;
}
