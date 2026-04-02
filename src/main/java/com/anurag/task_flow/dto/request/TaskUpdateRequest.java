package com.anurag.task_flow.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskUpdateRequest {
  String title;
  String description;
  LocalDate dueDate;
  Long userId;
}
