package com.personal.allo_backend_test.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseConstant {
  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_FAILED = "failed";
  public static final String MESSAGE_RESOURCE_TYPE_NOT_SUPPORTED = "resourceType %s not supported";
}
