package com.anurag.task_flow.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
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
import com.anurag.task_flow.entity.Task;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.BadRequestException;
import com.anurag.task_flow.security.CustomUserDetails;
import com.anurag.task_flow.service.TaskService;
import com.anurag.task_flow.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;
  private final UserService userService;

  public TaskController(TaskService taskService, UserService userService) {
    this.taskService = taskService;
    this.userService = userService;
  }

  private TaskResponse mapToResponse(Task task) {
    TaskResponse response = new TaskResponse();
    response.setId(task.getId());
    response.setTitle(task.getTitle());
    response.setDescription(task.getDescription());
    response.setCompleted(task.isCompleted());
    response.setUserId(task.getAssignedUser().getId());
    response.setStatus(task.isCompleted() ? "DONE" : "PENDING");
    response.setDueDate(task.getDueDate());
    return response;
  }

  // working
  @PostMapping
  @PreAuthorize("#request.getUserId() == authentication.principal.getUserId() or hasRole('ADMIN')")
  public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
    User user = userService.getUserById(request.getUserId());

    Task task = new Task();
    task.setAssignedUser(user);
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setDueDate(request.getDueDate());
    task.setCompleted(false);

    Task savedTask = taskService.createTask(task);
    TaskResponse response = mapToResponse(savedTask);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // working
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<TaskResponse>> getAllTask() {
    List<Task> tasks = taskService.getAllTasks();
    List<TaskResponse> response = new ArrayList<>();
    for (int i = 0; i < tasks.size(); i++) {
      response.add(mapToResponse(tasks.get(i)));
    }
    return ResponseEntity.ok(response);
  }

  // working
  @PatchMapping("/{id}/status")
  public ResponseEntity<TaskResponse> toggleStatus(@PathVariable Long id, Authentication authentication) {

    CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
    Task updatedTask = taskService.toggleTask(id, currentUser);
    return ResponseEntity.ok(mapToResponse(updatedTask));
  }

  // working
  @GetMapping("/{id}")
  @PreAuthorize("#id == authentication.principal.getUserId() or hasRole('ADMIN')")
  public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long id, Pageable pageable) {
    Page<Task> pageContent = taskService.getTasksByUser(id, pageable);
    List<TaskResponse> tasks = pageContent.getContent().stream().map((ele) -> mapToResponse(ele)).toList();
    return ResponseEntity.ok(tasks);
  }

  // working
  @PatchMapping("/{taskId}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
      @RequestBody TaskUpdateRequest updatedTaskReq, Authentication authentication) {
    CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
    Task response = taskService.updateTask(taskId, updatedTaskReq, currentUser);
    return ResponseEntity.ok(mapToResponse(response));
  }
}
