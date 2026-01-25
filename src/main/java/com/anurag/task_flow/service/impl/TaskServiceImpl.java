package com.anurag.task_flow.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.dto.request.TaskUpdateRequest;
import com.anurag.task_flow.entity.Task;
import com.anurag.task_flow.exception.BadRequestException;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.TaskRepository;
import com.anurag.task_flow.security.CustomUserDetails;
import com.anurag.task_flow.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  public TaskServiceImpl(TaskRepository taskRepository) {
    // injected here without autowired because only one constructor is there
    this.taskRepository = taskRepository;
  }

  @Override
  public Task createTask(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Task getTaskById(Long id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with Id: " + id));
  }

  @Override
  public Task toggleTask(Long id, CustomUserDetails currentUser) {
    Task foundTask = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    boolean isSameUser = currentUser.getUserId().equals(foundTask.getAssignedUser().getId());
    boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");
    if (!isSameUser && !isAdmin) {
      throw new AuthorizationDeniedException("Access Denied");
    }
    foundTask.setCompleted(!foundTask.isCompleted());
    return taskRepository.save(foundTask);
  }

  @Override
  public Page<Task> getTasksByUser(Long id, Pageable page) {
    Page<Task> tasks = taskRepository.findByAssignedUserId(id, page);
    return tasks;
  }

  @Override
  public Task updateTask(Long taskId, TaskUpdateRequest updatedTaskReq, CustomUserDetails currentUser) {
    Task updatedTask = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    boolean isSameUser = currentUser.getUserId().equals(updatedTask.getAssignedUser().getId());
    boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");

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
    return taskRepository.save(updatedTask);
  }

}
