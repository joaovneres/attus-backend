package br.com.attus.processos.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;
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
                contact     = @Contact(name = "Equipe Attus", email = "dev@attus.com.br")
        ),
        servers = @Server(url = "/", description = "Servidor local")
)
public class OpenApiConfig {

    /**
     * Documenta *todos* os controllers do pacote API.
     * Se quiser versão/filtro específico, crie mais grupos.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("attus-processos")
                .packagesToScan("br.com.attus.processos.api")
                .build();
    }
}
