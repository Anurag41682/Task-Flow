package com.anurag.task_flow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.task_flow.entity.PasswordSetupToken;
import com.anurag.task_flow.entity.User;

public interface PasswordSetupTokenRepository extends JpaRepository<PasswordSetupToken, Long> {
  Optional<PasswordSetupToken> findByToken(String token);

  Optional<PasswordSetupToken> findByUser(User user);
}
