package com.allobank.allobackendtest.Response;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> data;
    private Integer totalPage;
    private Integer Page;
    private Integer Size;
    private Long TotalElement;

    public PageResponse(org.springframework.data.domain.Page<T> page){
        this.data = page.getContent();
        this.totalPage =page.getTotalPages();
        this.Page = page.getNumber();
        this.Size = page.getSize();
        this.TotalElement = page.getTotalElements();
    }
}
