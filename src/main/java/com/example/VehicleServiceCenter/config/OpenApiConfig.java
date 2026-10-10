package com.example.VehicleServiceCenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleServiceCenterOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("Vehicle Service Center Management System")
                        .version("1.0.0")
                        .description(
                                "REST API documentation for the "
                                + "Vehicle Service Center Management System."
                        ))

                .components(new Components()
                        .addSecuritySchemes(
                                "basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                        )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("basicAuth")
                );
    }
}