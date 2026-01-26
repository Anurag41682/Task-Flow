package com.anurag.task_flow.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.task_flow.dto.request.TaskRequest;
import com.anurag.task_flow.dto.request.TaskUpdateRequest;
import com.anurag.task_flow.dto.response.TaskResponse;
import com.anurag.task_flow.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  @PreAuthorize("#request.getUserId() == authentication.principal.getUserId() or hasRole('ADMIN')")
  public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
    TaskResponse response = taskService.createTask(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<TaskResponse>> getAllTask(Pageable pageable) {
    List<TaskResponse> response = taskService.getAllTasks(pageable);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{taskId}/status")
  public ResponseEntity<TaskResponse> toggleStatus(@PathVariable Long taskId, Authentication authentication) {
    TaskResponse response = taskService.toggleTask(taskId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{userId}")
  @PreAuthorize("#userId == authentication.principal.getUserId() or hasRole('ADMIN')")
  public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId, Pageable pageable) {
    List<TaskResponse> response = taskService.getTasksByUser(userId, pageable);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{taskId}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
      @RequestBody TaskUpdateRequest updatedTaskReq) {
    TaskResponse response = taskService.updateTask(taskId, updatedTaskReq);
    return ResponseEntity.ok(response);
  }
}
