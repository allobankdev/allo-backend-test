package id.co.microservice.currency.currency_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("My Spring Boot 3 API")
                .description("Backend Service for Finance Microservice")
                .version("1.0.0"))
            .servers(List.of(
                new Server().url("http://localhost:9990")))
            ;
    }

}
