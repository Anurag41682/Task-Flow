package com.anurag.task_flow.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.anurag.task_flow.dto.request.TaskRequest;
import com.anurag.task_flow.dto.request.TaskUpdateRequest;
import com.anurag.task_flow.dto.response.TaskResponse;

public interface TaskService {
  TaskResponse createTask(TaskRequest taskReq);

  List<TaskResponse> getAllTasks(Pageable pageable);

  TaskResponse toggleTask(Long taskId);

  List<TaskResponse> getTasksByUser(Long userId, Pageable pageable);

  TaskResponse updateTask(Long taskId, TaskUpdateRequest updatedTask);

}
