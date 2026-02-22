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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        long start = System.currentTimeMillis();

        try {
            log.info("======================================");
            log.info("Incoming request: method={}, uri={}",
                    request.getMethod(),
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);


        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            String responseBody = logResponse(wrapResponse(response));
            log.info("responseBody:{}", responseBody);
            log.info("=======================================");
            long duration = System.currentTimeMillis() - start;
            log.info("Response sent: status={}, duration={} ms",
                    response.getStatus(),
                    duration
            );
        }
    }

    private String logResponse(ContentCachingResponseWrapper response) {
        return new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        return response instanceof ContentCachingResponseWrapper ? (ContentCachingResponseWrapper) response : new ContentCachingResponseWrapper(response);
    }
}
