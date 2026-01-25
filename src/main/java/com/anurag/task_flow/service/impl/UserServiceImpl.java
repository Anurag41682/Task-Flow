package com.anurag.task_flow.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.anurag.task_flow.entity.PasswordSetupToken;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.BadRequestException;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.PasswordSetupTokenRepository;
import com.anurag.task_flow.repository.UserRepository;
import com.anurag.task_flow.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordSetupTokenRepository passwordSetupTokenRepository;

  public UserServiceImpl(UserRepository userRepository, PasswordSetupTokenRepository passwordSetupTokenRepository) {
    // userRepository bean injected here without autowired because only one
    // constructor is there
    this.userRepository = userRepository;
    this.passwordSetupTokenRepository = passwordSetupTokenRepository;
  }

  private void generateToken(User savedUser) {
    PasswordSetupToken passwordSetupToken = passwordSetupTokenRepository.findByUser(savedUser)
        .orElse(new PasswordSetupToken());

    passwordSetupToken.setToken(UUID.randomUUID().toString());
    passwordSetupToken.setUser(savedUser);
    passwordSetupToken.setExpiry(LocalDateTime.now().plusMinutes(30));

    passwordSetupTokenRepository.save(passwordSetupToken);

    System.out.println(
        "Password setup link: http://localhost:8080/api/auth/set-password?token="
            + passwordSetupToken.getToken());
  }

  @Override
  public User createUser(User user) {

    Optional<User> isUserExisting = userRepository.findByEmail(user.getEmail());

    if (isUserExisting.isPresent()) {
      User existingUser = isUserExisting.get();
      if (existingUser.isEnabled()) {
        throw new BadRequestException("Email already exists");
      }
      generateToken(existingUser);
      return existingUser;
    }

    User savedUser = userRepository.save(user);
    generateToken(savedUser);
    return savedUser;
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
  }

}
