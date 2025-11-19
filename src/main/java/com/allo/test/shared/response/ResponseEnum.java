package com.allo.test.shared.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseEnum {
  SUCCESS("success", "Success", HttpStatus.OK),

  INVALID_PARAM("invalid_param", "Invalid Parameter", HttpStatus.BAD_REQUEST),
  INVALID_RESOURCE_TYPE("invalid_resource_type", "Invalid Resource Type", HttpStatus.BAD_REQUEST),
  MISSING_REQUIRED_CONFIG("missing_required_config", "Config is Missing", HttpStatus.NOT_IMPLEMENTED),
  EXTERNAL_API_UNAVAILABLE("external_api_unavailable", "External API Unavailable", HttpStatus.SERVICE_UNAVAILABLE),
  EXTERNAL_API_ERROR("external_api_error", "External API is Error", HttpStatus.BAD_GATEWAY),
  INVALID_EXTERNAL_API_RESPONSE("invalid_external_api_response", "Invalid External API Response", HttpStatus.BAD_GATEWAY),
  DATA_NOT_AVAILABLE("data_not_available", "Data Not Available", HttpStatus.SERVICE_UNAVAILABLE),
  INTERNAL_SERVER_ERROR("internal_server_error", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String responseCode;
  private final String responseMessage;
  private final HttpStatus httpStatus;

}
