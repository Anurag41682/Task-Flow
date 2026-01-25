package com.anurag.task_flow.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anurag.task_flow.dto.request.TaskRequest;
import com.anurag.task_flow.dto.request.TaskUpdateRequest;
import com.anurag.task_flow.dto.response.TaskResponse;
import com.anurag.task_flow.entity.Task;
import com.anurag.task_flow.security.CustomUserDetails;

public interface TaskService {
  TaskResponse createTask(TaskRequest taskReq);

  // Task getTaskById(Long id);

  List<TaskResponse> getAllTasks();

  TaskResponse toggleTask(Long id);

  Page<Task> getTasksByUser(Long id, Pageable pageable);

  Task updateTask(Long taskId, TaskUpdateRequest updatedTask, CustomUserDetails customUserDetails);

}
