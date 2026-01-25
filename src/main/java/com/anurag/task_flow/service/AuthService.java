package com.anurag.task_flow.service;

import com.anurag.task_flow.dto.request.LoginRequest;
import com.anurag.task_flow.dto.request.SetPasswordRequest;
import com.anurag.task_flow.dto.request.SignupRequest;
import com.anurag.task_flow.dto.response.JwtResponse;

public interface AuthService {
  void signup(SignupRequest signupReq);

  JwtResponse login(LoginRequest loginReq);

  void setPassword(String token, SetPasswordRequest setPasswordRequest);
}
