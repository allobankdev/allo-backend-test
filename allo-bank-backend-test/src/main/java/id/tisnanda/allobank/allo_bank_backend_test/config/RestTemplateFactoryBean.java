package id.tisnanda.allobank.allo_bank_backend_test.config;

import id.tisnanda.allobank.allo_bank_backend_test.middleware.RestClientLoggingMiddleware;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {


    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Value("${frankfurter.api.timeout}")
    private int timeout;

    @Value("${frankfurter.api.headers.Accept}")
    private String acceptHeader;

    @Autowired(required = false)
    RestClientLoggingMiddleware loggingMiddleware;

    // Flag untuk menentukan apakah logging aktif
    @Setter
    private boolean enableLogging = true;


    @Override
    public RestTemplate getObject() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);

        RestTemplate restTemplate = new RestTemplate(factory);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().set("Accept", acceptHeader);
            return execution.execute(request, body);
        });

        if (enableLogging && loggingMiddleware != null) {
            interceptors.add(loggingMiddleware);
        }

        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }


}
