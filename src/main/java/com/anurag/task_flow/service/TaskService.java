package com.anurag.task_flow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anurag.task_flow.entity.Task;

public interface TaskService {
  Task createTask(Task task);

  Task getTaskById(Long id);

  List<Task> getAllTasks();

  Task toggleTask(Long id);

  Page<Task> getTasksByUser(Long id, Pageable pageable);

  Task updateTask(Task updatedTask);

}
