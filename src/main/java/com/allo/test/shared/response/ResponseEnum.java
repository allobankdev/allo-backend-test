package com.allo.test.shared.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseEnum {
  SUCCESS("success", "success", HttpStatus.OK),

  INVALID_PARAM("invalid_param", "invalid.param", HttpStatus.BAD_REQUEST),
  INVALID_RESOURCE_TYPE("invalid_resource_type", "invalid.resource.type", HttpStatus.BAD_REQUEST),
  MISSING_REQUIRED_CONFIG("missing_required_config", "missing.required.config", HttpStatus.NOT_IMPLEMENTED),
  EXTERNAL_API_UNAVAILABLE("external_api_unavailable", "external.api.unavailable", HttpStatus.SERVICE_UNAVAILABLE),
  EXTERNAL_API_ERROR("external_api_error", "external.api.error", HttpStatus.BAD_GATEWAY),
  INVALID_EXTERNAL_API_RESPONSE("invalid_external_api_response", "invalid.external.api.response", HttpStatus.BAD_GATEWAY),
  DATA_NOT_AVAILABLE("data_not_available", "data.not.available", HttpStatus.SERVICE_UNAVAILABLE),
  INTERNAL_SERVER_ERROR("internal_server_error", "internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String responseCode;
  private final String responseMessage;
  private final HttpStatus httpStatus;

}
