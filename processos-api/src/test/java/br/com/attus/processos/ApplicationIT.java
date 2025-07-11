package br.com.attus.processos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ApplicationIT {

    @Autowired
    ApplicationContext ctx;

    @Test
    @DisplayName("Contexto Spring deve inicializar sem falhar")
    void contextLoads() {
        assertThat(ctx).isNotNull();
    }
}
