package com.anurag.task_flow.security;

import java.time.Instant;

public class SecurityErrorResponse {
  public static String build(int status, String message) {
    return """
        {
          "status":"%d",
          "message": "%s",
          "timeStamp": "%s"
        }
        """.formatted(status, message, Instant.now());
  }
}
