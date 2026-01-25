package com.anurag.task_flow.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.anurag.task_flow.entity.Role;
import com.anurag.task_flow.entity.User;

public class CustomUserDetails implements UserDetails {

  private Long userId;
  private String email;
  private String password;
  private Role role;
  private boolean enabled;

  CustomUserDetails(User user) {
    this.userId = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.role = user.getRole();
    this.enabled = user.isEnabled();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public Long getUserId() {
    return userId;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

}
