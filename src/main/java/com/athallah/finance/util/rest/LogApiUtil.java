package com.athallah.finance.util.rest;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Slf4j
public class LogApiUtil {

    private LogApiUtil() {}

    public static void error(Throwable e, String action, String module) {
        var msg = String.format("Terjadi kesalahan. action: %s. module: %s.", action, module);
        log.error(msg, e);
    }

    public static void printLogErrorRestApi(
            Exception e, UriComponentsBuilder builder,
            HttpMethod httpMethod, HttpHeaders httpHeaders, Object reqDTO) {
        printLogErrorRestApi(log, e, builder, httpMethod, httpHeaders, reqDTO);
    }

    public static void printLogErrorRestApi(
            Logger logger, Exception e, UriComponentsBuilder builder,
            HttpMethod httpMethod, HttpHeaders httpHeaders, Object reqDTO) {
        var uriBuilder = builder.build(true).toUriString();
        logger.error("URL {}, HttpMethod {} ", uriBuilder, httpMethod);
        if (httpHeaders != null) {
            Set<String> keys = httpHeaders.keySet();
            var header = new StringBuilder();
            for (String key : keys) {
                if (header.length() != 0)
                    header.append(", ");
                header.append(key).append("=").append(httpHeaders.get(key));
            }
            logger.error("Request Header {}", header);
        }
        if(reqDTO != null)
            logger.error("Request Body {}", reqDTO);
        logger.error("Failed to hit ", e);
    }
}

