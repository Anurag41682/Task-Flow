package com.anurag.task_flow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  // database generate the primary key automatically using its auto-increment
  // feature (increment the value of id of each column by one each time they
  // created)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  // makes sure email are unique from the db side otherwise throws error
  @Column(unique = true)
  private String email;

}
