package com.example.allobank.project.config;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;


@Configuration
public class ApplicationConfig {
	
	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
	    return new ForwardedHeaderFilter();
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return new RestTemplate();
    }
	
	@Bean
	public SecurityUtil getSecurityUtil() {
		return new SecurityUtil();
	}

}
