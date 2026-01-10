package com.anurag.task_flow.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.task_flow.dto.request.TaskRequest;
import com.anurag.task_flow.dto.response.TaskResponse;
import com.anurag.task_flow.entity.Task;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.service.TaskService;
import com.anurag.task_flow.service.UserService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;
  private final UserService userService;

  private TaskResponse mapToResponse(Task task) {
    TaskResponse response = new TaskResponse();
    response.setId(task.getId());
    response.setTitle(task.getTitle());
    response.setDescription(task.getDescription());
    response.setCompleted(task.isCompleted());
    response.setUserId(task.getAssignedUser().getId());
    response.setStatus(task.isCompleted() ? "DONE" : "PENDING");

    return response;
  }

  public TaskController(TaskService taskService, UserService userService) {
    this.taskService = taskService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
    User user = userService.getUserById(request.getUserId());

    Task task = new Task();
    task.setAssignedUser(user);
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setCompleted(false);

    Task savedTask = taskService.createTask(task);
    TaskResponse response = mapToResponse(savedTask);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> getAllTask() {
    List<Task> tasks = taskService.getAllTasks();

    List<TaskResponse> response = new ArrayList<>();
    for (int i = 0; i < tasks.size(); i++) {
      response.add(mapToResponse(tasks.get(i)));
    }

    return ResponseEntity.ok(response);

  }

}
