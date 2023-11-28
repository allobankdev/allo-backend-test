package com.allobank.allobackendtest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationDTO {
  private Integer page;
  private Integer limit;
  private String sortBy;
  private String sortOrder;

}
