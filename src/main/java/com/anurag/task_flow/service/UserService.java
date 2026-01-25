package com.anurag.task_flow.service;

import java.util.List;

import com.anurag.task_flow.dto.request.UserRequest;
import com.anurag.task_flow.dto.response.UserResponse;
import com.anurag.task_flow.entity.User;

public interface UserService {
  UserResponse createUser(UserRequest userReq);

  List<User> getAllUsers();

  User getUserById(Long id);

}