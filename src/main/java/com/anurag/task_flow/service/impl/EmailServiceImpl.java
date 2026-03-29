package com.anurag.task_flow.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

  @Value("${resend.api.key}")
  private String resendApiKey;

  public void sendSetPasswordEmail(String toEmail, String link) {
    try {
      String body = """
          {
            "from": "noreply@agnomerf.store",
            "to": "%s",
            "subject": "Set Your Password",
            "text": "Click the link to set the password:\\n%s"
          }
          """.formatted(toEmail, link);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.resend.com/emails"))
          .header("Authorization", "Bearer " + resendApiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println("Resend response: " + response.body());

    } catch (Exception e) {
      System.out.println("Email error: " + e.getMessage());
    }
  }
}
