package com.allo.test.shared.response.template;

import com.allo.test.shared.response.attribute.ResponseSchemaAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

  @JsonProperty("responseSchema")
  private ResponseSchemaAttribute responseSchema;

  @JsonProperty("data")
  private T data;

}