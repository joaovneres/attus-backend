package br.com.attus.processos.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenApiConfigurationTest {

    @Autowired
    GroupedOpenApi groupedOpenApi;

    @Test
    @DisplayName("Bean GroupedOpenApi deve existir e possuir nome configurado")
    void beanCriado() {
        assertThat(groupedOpenApi).isNotNull();
        assertThat(groupedOpenApi.getGroup()).isEqualTo("attus-processos");
    }
}