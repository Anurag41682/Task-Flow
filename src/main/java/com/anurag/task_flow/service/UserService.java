package com.anurag.task_flow.service;

import java.util.List;

import com.anurag.task_flow.entity.User;

public interface UserService {
  User createUser(User user);

  List<User> getAllUsers();

  User getUserById(Long id);

}