package com.anurag.task_flow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.task_flow.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  List<User> findByEnabledTrue();

  Page<User> findByEnabledTrue(Pageable pageable);

}
