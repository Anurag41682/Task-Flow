package com.anurag.task_flow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.task_flow.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
