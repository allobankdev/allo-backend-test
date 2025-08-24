package com.allobank.allobackendtest.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@Data
public class PageableDTO {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private String sortBy;
    private String sortDirection;

    public PageableDTO(Page<?> pageData) {
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();

        Sort.Order order = pageData.getSort().stream().findFirst().orElse(null);
        if (order != null) {
            this.sortBy = order.getProperty();
            this.sortDirection = order.getDirection().toString();
        } else {
//            default
            this.sortBy = "id";
            this.sortDirection = "asc";
        }
    }
}
