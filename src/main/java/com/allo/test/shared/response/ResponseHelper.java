package com.allo.test.shared.response;

import com.allo.test.shared.response.attribute.ErrorAttribute;
import com.allo.test.shared.response.template.ResponseData;
import com.allo.test.shared.response.template.ResponseError;
import com.allo.test.shared.response.template.ResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseHelper {

  private final ResponseMessageHelper responseMessageHelper;

  /**
   * a utility function that is used to generate detailed responses
   * @param responseEnum Enum Response
   * @param <T> Generic Type, for class body return
   * @return ResponseEntity contain Template Response
   */
  public <T> ResponseEntity<ResponseData<T>> createResponseData(
          ResponseEnum responseEnum,
          T data
  ) {
    return ResponseEntity
            .status(responseEnum.getHttpStatus())
            .body(ResponseData.<T>builder()
                    .data(data)
                    .build());
  }

  public <T> ResponseEntity<ResponseList<T>> createResponseList(
          ResponseEnum responseEnum,
          List<T> list
  ) {
    return ResponseEntity
            .status(responseEnum.getHttpStatus())
            .body(ResponseList.<T>builder()
                    .responseSchemaAttribute(responseMessageHelper.getResponseSchema(responseEnum))
                    .data(list)
                    .build());
  }

  public ResponseEntity<ResponseError> createResponseError(
      ResponseEnum responseEnum,
      List<ErrorAttribute> errorAttributes
  ) {
    return ResponseEntity
            .status(responseEnum.getHttpStatus())
            .body(ResponseError.builder()
                    .responseSchema(responseMessageHelper.getResponseSchema(responseEnum))
                    .errors(errorAttributes)
                    .build());
  }

}
