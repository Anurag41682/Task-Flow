package com.anurag.task_flow.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anurag.task_flow.service.EmailService;

import javax.net.ssl.SSLContext;
import java.time.Duration;

@Service
public class EmailServiceImpl implements EmailService {

  @Value("${resend.api.key}")
  private String resendApiKey;

  public void sendSetPasswordEmail(String toEmail, String link) {
    try {
      // Log to verify key is loaded
      System.out.println("Using Resend key: " + (resendApiKey != null ? resendApiKey.substring(0, 6) + "..." : "NULL"));

      String body = """
          {
            "from": "noreply@agnomerf.store",
            "to": "%s",
            "subject": "Set Your Password",
            "text": "Click the link to set the password:\\n%s"
          }
          """.formatted(toEmail, link);

      // Force TLS 1.2
      SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
      sslContext.init(null, null, null);

      HttpClient client = HttpClient.newBuilder()
          .sslContext(sslContext)
          .connectTimeout(Duration.ofSeconds(10))
          .build();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.resend.com/emails"))
          .header("Authorization", "Bearer " + resendApiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println("Resend response: " + response.statusCode() + " " + response.body());

    } catch (Exception e) {
      System.out.println("Email error: " + e.getMessage());
      e.printStackTrace(); // Full stack trace
    }
  }
}