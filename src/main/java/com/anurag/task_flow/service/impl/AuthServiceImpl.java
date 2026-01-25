package com.anurag.task_flow.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.dto.request.SetPasswordRequest;
import com.anurag.task_flow.dto.request.SignupRequest;
import com.anurag.task_flow.dto.request.UserRequest;
import com.anurag.task_flow.entity.PasswordSetupToken;
import com.anurag.task_flow.entity.Role;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.exception.BadRequestException;
import com.anurag.task_flow.repository.PasswordSetupTokenRepository;
import com.anurag.task_flow.repository.UserRepository;
import com.anurag.task_flow.security.AuthResult;
import com.anurag.task_flow.security.CustomUserDetails;
import com.anurag.task_flow.security.JwtTokenProvider;
import com.anurag.task_flow.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtTokenProvider jwtTokenProvider;
  private AuthenticationManager authenticationManager;
  private PasswordSetupTokenRepository passwordSetupTokenRepository;

  public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager,
      PasswordSetupTokenRepository passwordSetupTokenRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticationManager = authenticationManager;
    this.passwordSetupTokenRepository = passwordSetupTokenRepository;
  }

  @Override
  public void signup(SignupRequest signupReq) {

    userRepository.findByEmail(signupReq.getEmail()).ifPresent(element -> {
      throw new BadRequestException("Email already exists");
    });
    User user = new User();
    user.setName(signupReq.getName());
    user.setEmail(signupReq.getEmail());
    user.setEnabled(true);
    user.setPassword(passwordEncoder.encode(signupReq.getPassword()));
    user.setRole(Role.ROLE_USER);
    userRepository.save(user);
  }

  @Override
  public AuthResult login(User user) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    String token = jwtTokenProvider.generateToken(authentication);

    // UserDetailsService.loadUserByUsername() is called automatically by Spring
    // Security as part of authentication that is when
    // AuthenticationManager.authenticate() is called and that gives us this
    // customUserDetails as a return
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    AuthResult response = new AuthResult();
    response.setEmail(customUserDetails.getUsername());
    response.setJwtToken(token);
    response.setRole(customUserDetails.getRole());
    response.setUserId(customUserDetails.getUserId());

    return response;
  }

  @Override
  public void setPassword(String token, SetPasswordRequest setPasswordRequest) {
    PasswordSetupToken passwordSetupToken = passwordSetupTokenRepository.findByToken(token)
        .orElseThrow(() -> new BadRequestException("Invalid token"));

    if (passwordSetupToken.getExpiry().isBefore(LocalDateTime.now())) {
      throw new BadRequestException("Token expired");
    }

    User user = passwordSetupToken.getUser();
    user.setPassword(passwordEncoder.encode(setPasswordRequest.getNewPassword()));
    user.setEnabled(true);

    userRepository.save(user);
    passwordSetupTokenRepository.delete(passwordSetupToken);

  }

}
