package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegResponse;
import com.allobank.allobackendtest.dto.PagingResponse;
import com.allobank.allobackendtest.dto.SearchCalegRequest;
import com.allobank.allobackendtest.dto.WebResponse;
import com.allobank.allobackendtest.service.CalegService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalegController {
    private final CalegService calegService;

    @GetMapping(
            path = "v1/api/caleg",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<CalegResponse>>searchCaleg(
            @RequestParam(name = "dapil", required = false) String dapil,
            @RequestParam(name = "partai", required = false) String partai,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "nomorUrut") String sortBy
    ){
        SearchCalegRequest request = SearchCalegRequest.builder()
                .dapil(dapil)
                .partai(partai)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();

        Page<CalegResponse> calegResponses = calegService.searchCaleg(request);
        return WebResponse.<List<CalegResponse>>builder()
                .data(calegResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(calegResponses.getNumber())
                        .totalPage(calegResponses.getTotalPages())
                        .size(calegResponses.getSize())
                        .build())
                .build();
    }
}

