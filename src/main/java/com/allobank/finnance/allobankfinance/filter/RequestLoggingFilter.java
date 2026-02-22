package com.allobank.finnance.allobankfinance.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        try {
            log.info("======================================");
            log.info("Incoming request: method={}, uri={}",
                    request.getMethod(),
                    request.getRequestURI()
            );

            filterChain.doFilter(request, wrappedResponse);

        } finally {

            String responseBody = new String(
                    wrappedResponse.getContentAsByteArray(),
                    StandardCharsets.UTF_8
            );

            log.info("responseBody: {}", responseBody);

            long duration = System.currentTimeMillis() - start;
            log.info("Response sent: status={}, duration={} ms",
                    wrappedResponse.getStatus(),
                    duration
            );

            log.info("======================================");

            wrappedResponse.copyBodyToResponse();
        }
    }
}
