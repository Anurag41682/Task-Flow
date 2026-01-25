package com.anurag.task_flow.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// for handling 403 exceptions from security side which don't reach controllers
// not the preauthorize on controller methods but present in Security config
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.getWriter().write(SecurityErrorResponse.build(403, "You don't have permission to access this resource"));
  }
}
