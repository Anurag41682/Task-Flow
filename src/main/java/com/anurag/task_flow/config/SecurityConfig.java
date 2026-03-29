package com.anurag.task_flow.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.anurag.task_flow.security.CustomAccessDeniedHandler;
import com.anurag.task_flow.security.CustomAuthEntryPoint;
import com.anurag.task_flow.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAuthEntryPoint customAuthEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Value("${allowed.origins}")
  private String allowedOrigins;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomAuthEntryPoint customAuthEntryPoint,
      CustomAccessDeniedHandler customAccessDeniedHandler) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.customAuthEntryPoint = customAuthEntryPoint;
    this.customAccessDeniedHandler = customAccessDeniedHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.cors(cors -> cors.configurationSource(request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedOrigins(List.of(allowedOrigins));
      config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
      config.setAllowedHeaders(List.of("*"));
      config.setAllowCredentials(true);
      return config;
    }))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(
            ex -> ex.authenticationEntryPoint(customAuthEntryPoint).accessDeniedHandler(customAccessDeniedHandler))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
