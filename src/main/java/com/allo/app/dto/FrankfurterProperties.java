package com.allo.app.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "external-service.frankfurter")
public class FrankfurterProperties {

    private String url;
    private int timeout;

}
