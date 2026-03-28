package com.anurag.task_flow.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {
  private List<T> content;
  private int totalPages;
  private long totalElements;
  private int pageNumber;
}
