package com.allo.test.configs.exceptions;

import com.allo.test.modules.finance.exceptions.DataNotAvailableException;
import com.allo.test.modules.finance.exceptions.InvalidResourceTypeException;
import com.allo.test.shared.response.ResponseEnum;
import com.allo.test.shared.response.ResponseHelper;
import com.allo.test.shared.response.attribute.ErrorAttribute;
import com.allo.test.shared.response.template.ResponseError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  private final ResponseHelper responseHelper;

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ResponseError> handleException(GlobalException exception) {
    log.error("Error : {}", exception.getResponseEnum());
    return responseHelper.createResponseError(exception.getResponseEnum(), null);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ResponseError> handleExternalApiException(ExternalApiException exception) {
    log.error("External API error - Endpoint: {}, Type: {}",
            exception.getApiEndpoint(),
            exception.getClass().getSimpleName());
    return responseHelper.createResponseError(exception.getResponseEnum(), null);
  }

  @ExceptionHandler(DataNotAvailableException.class)
  public ResponseEntity<ResponseError> handleDataNotAvailable(DataNotAvailableException exception) {
    log.warn("Data not available - resource failed to load at startup");
    return responseHelper.createResponseError(exception.getResponseEnum(), null);
  }

  @ExceptionHandler(InvalidResourceTypeException.class)
  public ResponseEntity<ResponseError> handleInvalidResourceType(InvalidResourceTypeException exception) {
    log.warn("Invalid resource type requested");
    return responseHelper.createResponseError(exception.getResponseEnum(), null);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseError> handleException(
          Exception ex,
          HttpServletRequest request,
          HttpServletResponse response
  ) {
    Arrays.stream(ex.getStackTrace()).limit(5).forEach(logger::error);
    logger.error(ex.getMessage());
    return responseHelper.createResponseError(ResponseEnum.INTERNAL_SERVER_ERROR, null);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request
  ) {
    List<ErrorAttribute> errors = new ArrayList<>();
    ex.getFieldErrors().forEach(fieldError ->
            errors.add(ErrorAttribute.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
    );

    return ResponseEntity.badRequest()
            .body(responseHelper.createResponseError(ResponseEnum.INVALID_PARAM, errors));
  }

}
