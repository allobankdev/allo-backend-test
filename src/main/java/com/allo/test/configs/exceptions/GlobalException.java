package com.allo.test.configs.exceptions;

import com.allo.test.shared.response.ResponseEnum;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

  private final ResponseEnum responseEnum;

  public GlobalException(ResponseEnum responseEnum) {
    super(responseEnum.getResponseCode());
    this.responseEnum = responseEnum;
  }
}
