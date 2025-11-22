package com.personal.allo_backend_test.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {
  private String status;
  private T data;
  private String resourceType;
  private String message;
}
