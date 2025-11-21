package test.allo.backend.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;

@Component
public class frankfurterApiClient implements FactoryBean<WebClient> {

    @Value("${external.frankfurter.base-url}")
    String baseUrl;

    @Override
    public WebClient getObject() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(client);

        return WebClient.builder().baseUrl(baseUrl)
                .defaultHeaders(header -> header.addAll(headers))
                .clientConnector(connector)
                .build();

    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
