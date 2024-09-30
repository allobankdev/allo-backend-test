package com.allobank.allobackendtest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:rc.properties")
public class RCProp {

    @Autowired
    private Environment env;

    public String message(String rc) {
        StringBuilder builder = new StringBuilder("rc.").append(rc);
        String message = env.getProperty(builder.toString());
        return message;
    }
}
