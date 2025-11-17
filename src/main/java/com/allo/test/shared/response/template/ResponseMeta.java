package com.allo.test.shared.response.template;

import com.allo.test.shared.response.attribute.MetaAttribute;
import com.allo.test.shared.response.attribute.ResponseSchemaAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMeta<T> {

	@JsonProperty("responseSchema")
	private ResponseSchemaAttribute responseSchema;

	@JsonProperty("meta")
	private MetaAttribute meta;

	@JsonProperty("data")
	private List<T> data;

}