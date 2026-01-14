package com.anurag.task_flow.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.anurag.task_flow.entity.Task;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.TaskRepository;
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
  public Task toggleTask(Long id) {
    Task foundTask = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    foundTask.setCompleted(!foundTask.isCompleted());
    return taskRepository.save(foundTask);
  }

  @Override
  public Page<Task> getTasksByUser(Long id, Pageable page) {
    Page<Task> tasks = taskRepository.findByAssignedUserId(id, page);
    return tasks;
  }

  @Override
  public Task updateTask(Task updatedTask) {
    return taskRepository.save(updatedTask);
  }

}
