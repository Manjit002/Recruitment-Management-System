package com.mjt.recruitment.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token in the format: Bearer <token>")
                        )
                )
                .info(new Info()
                        .title("Recruitment Management System")
                        .description("Backend APIs for Recruitment Management System (signup, login, resume upload, jobs, admin)")
                        .version("1.5.0")
                        .contact(new Contact()
                                .name("Manjit Patel")
                                .email("manjitpatel002@gmail.com")
                                .url("https://www.linkedin.com/in/manjitpatel/")
                        )
                        .license(new License()
                                .name("License of APIs")
                                .url("https://github.com/Manjit002")
                        )
                );

    }
}
