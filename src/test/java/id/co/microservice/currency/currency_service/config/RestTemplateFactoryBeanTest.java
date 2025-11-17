package id.co.microservice.currency.currency_service.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

class RestTemplateFactoryBeanTest {

    @Test
    void testGetObject_CreatesRestTemplateWithConfiguredTimeouts() throws Exception {
        ExternalApiConfig externalApiConfig = new ExternalApiConfig();
        externalApiConfig.setConnectTimeout(3000);
        externalApiConfig.setReadTimeout(7000);

        RestTemplateFactoryBean factoryBean = new RestTemplateFactoryBean(externalApiConfig);

        RestTemplate restTemplate = factoryBean.getObject();

        assertNotNull(restTemplate);

        SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();

        assertNotNull(restTemplate);
        assertEquals(RestTemplate.class, factoryBean.getObjectType());
        assertEquals(true, factoryBean.isSingleton());
    }

    @Test
    void testGetObjectTypeAndSingleton() {
        ExternalApiConfig externalApiConfig = new ExternalApiConfig();
        RestTemplateFactoryBean factoryBean = new RestTemplateFactoryBean(externalApiConfig);

        assertEquals(RestTemplate.class, factoryBean.getObjectType());
        assertEquals(true, factoryBean.isSingleton());
    }
}
