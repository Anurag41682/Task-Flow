package com.anurag.task_flow.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
    return new CustomUserDetails(user);
  }

}
