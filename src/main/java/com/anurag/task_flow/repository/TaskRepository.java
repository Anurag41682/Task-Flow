package com.anurag.task_flow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.task_flow.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
