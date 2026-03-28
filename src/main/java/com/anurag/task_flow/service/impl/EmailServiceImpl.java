package com.anurag.task_flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender mailSender;

  public void sendSetPasswordEmail(String toEmail, String link) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(toEmail);
    mailMessage.setSubject("Set Your Password");
    mailMessage.setText("Clink the link to set the password:\n" + link);
    mailSender.send(mailMessage);
  }

}
