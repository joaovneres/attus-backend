package br.com.attus.processos.nucleo.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Ativa o recurso de auditoria do Spring Data JPA (@CreatedDate, @LastModifiedDate).
 * <p>
 * Se sua aplicação tiver autenticação, implemente {@link AuditorAware}
 * retornando o login do usuário.  Para o teste inicial, devolvemos
 * uma constante "system".
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // return () -> Optional.ofNullable(
        //    SecurityContextHolder.getContext()
        //                         .getAuthentication()
        //                         .getName());
        return () -> Optional.of("system");
    }
}
