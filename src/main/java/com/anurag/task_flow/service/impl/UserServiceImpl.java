package com.anurag.task_flow.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.UserRepository;
import com.anurag.task_flow.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    // userRepository bean injected here without autowired because only one
    // constructor is there
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
  }

}
