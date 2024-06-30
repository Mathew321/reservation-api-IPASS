package org.hu.reservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation API")
                        .version("1.0")
                        .description("API documentation for the Reservation system"))
                .servers(List.of(
                        new Server()
                                .description("Reservation API Server in Azure")
                                .url("http://localhost:8080")));
    }
}
