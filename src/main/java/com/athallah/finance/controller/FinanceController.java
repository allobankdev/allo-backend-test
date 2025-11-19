package com.athallah.finance.controller;

import com.athallah.finance.config.response_message.localization_messages.EnumMessagesKey;
import com.athallah.finance.service.FinanceService;
import com.athallah.finance.util.constant.ResourceType;
import com.athallah.finance.util.exception.GlobalException;
import com.athallah.finance.util.response.GlobalRespDto;
import com.athallah.finance.util.rest.LogApiUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Enumeration;

@RestController
@AllArgsConstructor
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getData(@PathVariable ResourceType resourceType,
                                          HttpServletRequest httpRequest) {
        try {
            var data = financeService.getData(resourceType);
            var response = GlobalRespDto.successResponseBuilder()
                    .data(data)
                    .message(EnumMessagesKey.DATA_FETCHED_SUCCESS.getMessageKey())
                    .build();
            return ResponseEntity.ok(response);
        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            logError(httpRequest, e, HttpMethod.PUT, null);
            throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, EnumMessagesKey.ERROR_INTERNAL_SERVER_ERROR.getMessageKey());
        }
    }

    private void logError(HttpServletRequest request, Exception e, HttpMethod method, Object body) {
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromRequest(request);
        HttpHeaders httpHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            httpHeaders.add(headerName, request.getHeader(headerName));
        }
        LogApiUtil.printLogErrorRestApi(e, uriBuilder, method, httpHeaders, body);
    }
}
