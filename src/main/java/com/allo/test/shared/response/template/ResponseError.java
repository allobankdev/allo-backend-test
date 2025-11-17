package com.allo.test.shared.response.template;

import com.allo.test.shared.response.attribute.ErrorAttribute;
import com.allo.test.shared.response.attribute.ResponseSchemaAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

  @JsonProperty("responseSchema")
  private ResponseSchemaAttribute responseSchema;

  @JsonProperty("errors")
  private List<ErrorAttribute> errors;

}
