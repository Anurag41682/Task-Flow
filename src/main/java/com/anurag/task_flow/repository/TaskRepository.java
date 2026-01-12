package com.anurag.task_flow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.task_flow.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

  // function name is significant here and depends on field in entityFiles

  // Page is an interface provided by spring data for pagination feature that
  // stores result

  // Pageable is also an interface but it is instruction that will be given to how
  // to paginate the data that we receieve
  Page<Task> findByAssignedUserId(Long userId, Pageable pageable);
}
