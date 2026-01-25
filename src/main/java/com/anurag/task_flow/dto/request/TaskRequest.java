package com.anurag.task_flow.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {
  @NotBlank
  private String title;
  private String description;
  private LocalDate dueDate;

  @NotNull
  private Long userId;
}
