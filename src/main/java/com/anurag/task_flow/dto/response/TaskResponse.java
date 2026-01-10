package com.anurag.task_flow.dto.response;

import lombok.Data;

@Data
public class TaskResponse {

  private Long id;
  private String title;
  private String description;
  private String status;
  private Boolean completed;
  private Long userId;
}
