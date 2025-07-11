/* src/test/java/br/com/attus/processos/event/ApplicationIT.java */
package br.com.attus.processos.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

/** Verifica se o contexto Spring do módulo eventos sobe sem falhas. */
@SpringBootTest
class EventApplicationIT {

    @Autowired ApplicationContext ctx;

    @Test
    @DisplayName("Contexto Spring deve ser inicializado")
    void contextLoads() {
        assertThat(ctx).isNotNull();
    }
}
