package com.allo.test.shared.response;

import com.allo.test.shared.response.attribute.ResponseSchemaAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseMessageHelper {

  private final MessageSource responseMessageSource;

  public ResponseSchemaAttribute getResponseSchema(ResponseEnum responseEnum) {
    return ResponseSchemaAttribute.builder()
            .responseCode(responseEnum.getResponseCode())
            .responseMessage(responseEnum.getResponseMessage())
            .build();
  }

}
