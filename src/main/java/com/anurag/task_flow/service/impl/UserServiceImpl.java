package com.anurag.task_flow.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.dto.request.UserRequest;
import com.anurag.task_flow.dto.response.UserResponse;
import com.anurag.task_flow.entity.PasswordSetupToken;
import com.anurag.task_flow.entity.Role;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.BadRequestException;
import com.anurag.task_flow.exception.ResourceNotFoundException;
import com.anurag.task_flow.repository.PasswordSetupTokenRepository;
import com.anurag.task_flow.repository.UserRepository;
import com.anurag.task_flow.service.EmailService;
import com.anurag.task_flow.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordSetupTokenRepository passwordSetupTokenRepository;
  private final EmailService emailService;

  @Value("${allowed.origins}")
  private String baseURL;

  public UserServiceImpl(UserRepository userRepository, PasswordSetupTokenRepository passwordSetupTokenRepository,
      EmailService emailService) {
    // userRepository bean injected here without autowired because only one
    // constructor is there
    this.userRepository = userRepository;
    this.passwordSetupTokenRepository = passwordSetupTokenRepository;
    this.emailService = emailService;
  }

  private UserResponse mapToUserResponse(User user) {
    UserResponse response = new UserResponse();
    response.setEmail(user.getEmail());
    response.setName(user.getName());
    response.setId(user.getId());
    return response;
  }

  private void generateToken(User savedUser) {
    PasswordSetupToken passwordSetupToken = passwordSetupTokenRepository.findByUser(savedUser)
        .orElse(new PasswordSetupToken());

    passwordSetupToken.setToken(UUID.randomUUID().toString());
    passwordSetupToken.setUser(savedUser);
    passwordSetupToken.setExpiry(LocalDateTime.now().plusMinutes(30));

    passwordSetupTokenRepository.save(passwordSetupToken);

    // System.out.println(
    // "Password setup link: http://localhost:5173/auth/set-password?token="
    // + passwordSetupToken.getToken());

    String link = baseURL + "set-password?token="
        + passwordSetupToken.getToken();

    emailService.sendSetPasswordEmail(savedUser.getEmail(), link);
  }

  @Override
  public UserResponse createUser(UserRequest userReq) {

    Optional<User> isUserExisting = userRepository.findByEmail(userReq.getEmail());

    if (isUserExisting.isPresent()) {
      User existingUser = isUserExisting.get();
      if (existingUser.isEnabled()) {
        throw new BadRequestException("Email already exists");
      }
      generateToken(existingUser);
      return mapToUserResponse(existingUser);
    }
    User user = new User();
    user.setEmail(userReq.getEmail());
    user.setName(userReq.getName());
    user.setRole(Role.ROLE_USER);
    User savedUser = userRepository.save(user);
    generateToken(savedUser);
    return mapToUserResponse(savedUser);
  }

  @Override
  public List<UserResponse> getAllUsersPaginated(Pageable pageable) {

    Pageable safePageable = PageRequest.of(pageable.getPageNumber(), Math.min(pageable.getPageSize(), 10));

    Page<User> users = userRepository.findByEnabledTrue(safePageable);
    List<UserResponse> response = users.getContent().stream().map(ele -> mapToUserResponse(ele)).toList();
    return response;
  }

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findByEnabledTrue();
    List<UserResponse> response = users.stream().map((ele) -> mapToUserResponse(ele)).toList();
    return response;
  }

  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return mapToUserResponse(user);
  }

}
