package com.anurag.task_flow.service;

public interface EmailService {
  void sendSetPasswordEmail(String toEmail, String link);
}
