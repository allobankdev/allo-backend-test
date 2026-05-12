package com.allobankdev.split_bill_api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI splitBillOpenAPI() {

        Server localServer = new Server();

        localServer.setUrl("http://localhost:4110");

        localServer.setDescription(
                "Local Development Server"
        );

        Contact contact = new Contact();

        contact.setName("Joko Kusnandi");

        contact.setEmail("jokokusnandi2@gmail.com");

        contact.setUrl(
                "https://github.com/JokoKusnandi"
        );

        License license = new License();

        license.setName("MIT License");

        license.setUrl(
                "https://opensource.org/licenses/MIT"
        );

        Info info = new Info()
                .title("Split Bill API")
                .version("1.0.0")
                .description("""
                        REST API for managing shared group expenses
                        and calculating optimized settlements.

                        Features:
                        - Create bill groups
                        - Add participants
                        - Add expenses
                        - Equal split calculation
                        - Optimized debt settlement
                        - Service charge calculation
                        - Swagger/OpenAPI documentation
                        """)
                .contact(contact)
                .license(license);

        ExternalDocumentation externalDocs =
                new ExternalDocumentation();

        externalDocs.setDescription(
                "GitHub Repository"
        );

        externalDocs.setUrl(
                "https://github.com/JokoKusnandi/split-bill-api"
        );

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .externalDocs(externalDocs);
    }
}
