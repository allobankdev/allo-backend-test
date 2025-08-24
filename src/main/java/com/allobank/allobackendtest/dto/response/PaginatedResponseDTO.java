package com.allobank.allobackendtest.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private PageableDTO metadata;

    public PaginatedResponseDTO(Page<T> pageData) {
        this.content = pageData.getContent();
        this.metadata = new PageableDTO(pageData);
    }
}
