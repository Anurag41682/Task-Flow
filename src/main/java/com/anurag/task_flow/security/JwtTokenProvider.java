package com.anurag.task_flow.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiry}")
  private Long jwtExpiry;

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(Authentication authentication) {
    String userName = authentication.getName();
    Date dateNow = new Date();
    Date dateExpiry = new Date(dateNow.getTime() + jwtExpiry);

    return Jwts.builder()
        .subject(userName)
        .issuedAt(dateNow)
        .expiration(dateExpiry)
        .signWith(getSigningKey())
        .compact();
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public boolean validateToken(String token) {
    Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
    return true;
  }

}
