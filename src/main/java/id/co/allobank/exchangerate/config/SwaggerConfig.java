package id.co.allobank.exchangerate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Value("${open-api.title}")
    private String openApiTitle;

    @Value("${open-api.description}")
    private String openApiDescription;

    @Value("${open-api.version}")
    private String openApiVersion;

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(openApiTitle)
                        .description(openApiDescription)
                        .version(openApiVersion));
    }
}