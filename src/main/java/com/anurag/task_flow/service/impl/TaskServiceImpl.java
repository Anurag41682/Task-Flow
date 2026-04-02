package com.anurag.task_flow.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.dto.request.TaskRequest;
import com.anurag.task_flow.dto.request.TaskUpdateRequest;
import com.anurag.task_flow.dto.response.PageResponse;
import com.anurag.task_flow.dto.response.TaskResponse;
import com.anurag.task_flow.entity.Task;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.BadRequestException;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.TaskRepository;
import com.anurag.task_flow.repository.UserRepository;
import com.anurag.task_flow.security.CustomUserDetails;
import com.anurag.task_flow.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
    // injected here without autowired because only one constructor is there
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
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
    response.setUserName(task.getAssignedUser().getName());
    return response;
  }

  @Override
  public TaskResponse createTask(TaskRequest taskReq) {
    User user = userRepository.findById(taskReq.getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id:" + taskReq.getUserId()));

    Task task = new Task();
    task.setAssignedUser(user);
    task.setCompleted(false);
    task.setDescription(taskReq.getDescription());
    task.setTitle(taskReq.getTitle());
    task.setDueDate(taskReq.getDueDate());
    Task savedTask = taskRepository.save(task);
    return mapToResponse(savedTask);
  }

  @Override
  public PageResponse<TaskResponse> getAllTasks(Pageable pageable) {

    Pageable safePageable = PageRequest.of(pageable.getPageNumber(), Math.min(pageable.getPageSize(), 10));

    Page<Task> tasks = taskRepository.findAll(safePageable);

    List<TaskResponse> content = tasks.getContent().stream().map(ele -> mapToResponse(ele)).toList();
    return new PageResponse<TaskResponse>(content, tasks.getTotalPages(), tasks.getTotalElements(), tasks.getNumber());
  }

  @Override
  public TaskResponse toggleTask(Long taskId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

    Task foundTask = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    boolean isSameUser = userDetails.getUserId().equals(foundTask.getAssignedUser().getId());
    boolean isAdmin = userDetails.getRole().name().equals("ROLE_ADMIN");
    if (!isSameUser && !isAdmin) {
      throw new AuthorizationDeniedException("Access Denied");
    }
    foundTask.setCompleted(!foundTask.isCompleted());
    Task savedTask = taskRepository.save(foundTask);
    return mapToResponse(savedTask);
  }

  @Override
  public List<TaskResponse> getTasksByUser(Long userId, Pageable pageable) {

    Pageable safePageable = PageRequest.of(pageable.getPageNumber(), Math.min(pageable.getPageSize(), 10));

    Page<Task> tasks = taskRepository.findByAssignedUserId(userId, safePageable);
    List<TaskResponse> tasksRes = tasks.getContent().stream().map(ele -> mapToResponse(ele)).toList();
    return tasksRes;
  }

  @Override
  public TaskResponse updateTask(Long taskId, TaskUpdateRequest updatedTaskReq) {
    Task updatedTask = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails currentUser = (CustomUserDetails) auth.getPrincipal();
    boolean isSameUser = currentUser.getUserId().equals(updatedTask.getAssignedUser().getId());
    boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");
    System.out.println("hi");
    if (!isSameUser && !isAdmin) {
      throw new AuthorizationDeniedException("Access Denied");
    }

    if (updatedTaskReq.getTitle() != null) {
      if (updatedTaskReq.getTitle().isBlank()) {
        throw new BadRequestException("Title can't be blank");
      }
      updatedTask.setTitle(updatedTaskReq.getTitle());
    }
    if (updatedTaskReq.getDescription() != null) {
      updatedTask.setDescription(updatedTaskReq.getDescription());
    }
    if (updatedTaskReq.getDueDate() != null) {
      updatedTask.setDueDate(updatedTaskReq.getDueDate());
    }
    if (updatedTaskReq.getUserId() != null) {
      User newUser = userRepository.findById(updatedTaskReq.getUserId())
          .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updatedTaskReq.getUserId()));
      updatedTask.setAssignedUser(newUser);
    }
    return mapToResponse(taskRepository.save(updatedTask));
  }

  @Override
  public void deleteTask(Long taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

    boolean isSameUser = userDetails.getUserId().equals(task.getAssignedUser().getId());
    boolean isAdmin = userDetails.getRole().name().equals("ROLE_ADMIN");

    if (!isSameUser && !isAdmin) {
      throw new AuthorizationDeniedException("Access Denied");
    }

    taskRepository.delete(task);
  }

}
