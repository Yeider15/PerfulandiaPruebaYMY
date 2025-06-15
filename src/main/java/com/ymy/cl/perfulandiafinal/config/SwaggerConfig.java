package com.ymy.cl.perfulandiafinal.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Perfulandia API")
                        .version("1.0.0")
                        .description("API para la gestión de Perfulandia, incluyendo usuarios, productos y ventas."));
    }

}
