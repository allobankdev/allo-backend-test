package com.allo.test.shared.response.attribute;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaAttribute {

	@JsonProperty("page")
	private int page;

	@JsonProperty("size")
	private int size;

	@JsonProperty("total")
	private long total;

}