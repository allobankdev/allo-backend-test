package achlaq.co.allo_backend_test.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties props;
    private WebClient webClient;

    @Override
    public WebClient getObject() {
        if (webClient == null) {
            webClient = WebClient.builder()
                    .baseUrl(props.getBaseUrl())
                    .build();
        }
        return webClient;
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
