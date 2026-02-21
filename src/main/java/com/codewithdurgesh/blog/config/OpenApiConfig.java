package com.codewithdurgesh.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                // 🌍 IMPORTANT → Railway HTTPS server (CORS fix)
                .addServersItem(new Server().url("https://blog-app-backend-production-f8e2.up.railway.app"))

                // 🔐 Security requirement
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // 🔐 JWT Security Scheme
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )

                // ℹ️ API Info
                .info(new Info()
                        .title("Blogging Application : Backend Course")
                        .version("1.0")
                        .description("Spring Boot Blog API Documentation"));
    }
}