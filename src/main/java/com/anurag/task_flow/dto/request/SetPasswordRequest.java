package com.anurag.task_flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetPasswordRequest {
  @NotNull
  @NotBlank
  String newPassword;
}
