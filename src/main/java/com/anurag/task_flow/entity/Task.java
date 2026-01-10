package com.anurag.task_flow.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {

  @Id
  // database generate the primary key automatically using its auto-increment
  // feature (increment the value of id of each column by one each time they
  // created)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String title;

  private String description;

  private LocalDate dueDate;

  private boolean completed = false;

  // Tells the type of relationship for this table
  @ManyToOne
  // Tells to create a column in this table named user_id which is foriegn key
  // that associates with the User table's primary key, It associate the table by
  // seeing the type of this object which in this case is User here
  @JoinColumn(name = "user_id")
  private User assignedUser;

}
