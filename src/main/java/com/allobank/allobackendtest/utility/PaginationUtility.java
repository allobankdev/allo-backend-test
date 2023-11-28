package com.allobank.allobackendtest.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtility {
  public static Pageable createPageable(int pageNumber, int limit, String sortField, String sortOrder) {
    Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortField);
    return PageRequest.of(pageNumber, limit, sort);
  }

  public static int TotalPages(Integer total, Integer limit) {
    var res = ((total - 1) / limit) + 1;
    return res <= 0 ? 1 : res;
  }

  public static Integer PageForQuery(Integer page) {
    if(page == null) return 0;
    return page < 1 ? 0 : page - 1;
  }

  public static Integer PageForView(Integer page) {
    if(page == null) return 1;
    return page <= 0 ? 1 : page;
  }
}
