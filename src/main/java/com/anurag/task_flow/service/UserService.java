package com.anurag.task_flow.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.anurag.task_flow.dto.request.UserRequest;
import com.anurag.task_flow.dto.response.UserResponse;

public interface UserService {
  UserResponse createUser(UserRequest userReq);

  List<UserResponse> getAllUsersPaginated(Pageable pageable);

  List<UserResponse> getAllUsers();

  UserResponse getUserById(Long id);

}