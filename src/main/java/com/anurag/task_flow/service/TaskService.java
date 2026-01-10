package com.anurag.task_flow.service;

import java.util.List;

import com.anurag.task_flow.entity.Task;

public interface TaskService {
  Task createTask(Task task);

  List<Task> getAllTasks();

}
