package id.co.microservice.currency.currency_service.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ApiClientConfig.class)
class ApiClientConfigTest {

    @MockitoBean
    private ExternalApiConfig externalApiConfig;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testRestTemplateFactoryBeanIsCreated() {
        RestTemplateFactoryBean bean = applicationContext.getBean(RestTemplateFactoryBean.class);

        assertNotNull(bean, "RestTemplateFactoryBean should be registered in the context");
    }

}