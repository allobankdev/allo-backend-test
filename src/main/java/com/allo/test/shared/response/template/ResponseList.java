package com.allo.test.shared.response.template;

import com.allo.test.shared.response.attribute.ResponseSchemaAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseList<T> {

  @JsonProperty("responseSchema")
  private ResponseSchemaAttribute responseSchemaAttribute;

  @JsonProperty("data")
  private List<T> data;

}