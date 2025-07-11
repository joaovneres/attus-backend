package br.com.attus.processos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Attus – Plataforma de Processos",
                version     = "v1",
                description = "API REST para gerenciamento de processos jurídicos",
                contact     = @Contact(name = "Equipe Attus",
                        email = "dev@attus.com.br")
        ),
        servers = @Server(url = "/", description = "Servidor local")
)
@SecurityScheme(
        name = "bearer-jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Autenticação via token JWT (header <code>Authorization: Bearer &lt;token&gt;</code>)"
)
public class OpenApiConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("attus-processos")
                .packagesToScan("br.com.attus.processos.api.controller")
                .build();
    }
}
