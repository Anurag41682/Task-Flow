package com.anurag.task_flow.service;

import com.anurag.task_flow.dto.request.SetPasswordRequest;
import com.anurag.task_flow.dto.request.SignupRequest;
import com.anurag.task_flow.entity.User;
import com.anurag.task_flow.security.AuthResult;

public interface AuthService {
  void signup(SignupRequest signupReq);

  AuthResult login(User user);

  void setPassword(String token, SetPasswordRequest setPasswordRequest);
}
