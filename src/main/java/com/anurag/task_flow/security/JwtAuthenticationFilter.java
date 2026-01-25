package com.anurag.task_flow.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// This filter fn does ->

// -Validates token
// -Extracts username
// -Loads user
// -Sets authentication context

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private JwtTokenProvider jwtTokenProvider;
  private UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      jwtTokenProvider.validateToken(token);

      String userName = jwtTokenProvider.getUsernameFromToken(token);

      UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
          userDetails.getAuthorities());

      // It sets the authenticated user into Spring Security’s context so
      // authorization and role checks can work for the current request.
      // All filters does this line which marks Request are authenticated
      SecurityContextHolder.getContext().setAuthentication(auth);

    }
    filterChain.doFilter(request, response);
  }

}
