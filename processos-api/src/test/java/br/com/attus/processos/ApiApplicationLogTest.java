package br.com.attus.processos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class ApiApplicationLogTest {

    @Test
    void deveLogarMensagemDeInicio(CapturedOutput output) {
        SpringApplication.run(ApiApplication.class);

        assertThat(output.getOut())
                .contains("Plataforma de Processos iniciada com sucesso!");
    }
}
