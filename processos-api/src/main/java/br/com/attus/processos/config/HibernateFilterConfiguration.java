package br.com.attus.processos.config;

import static java.util.Objects.requireNonNull;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateFilterConfiguration {

    public static final String ATIVO_FILTER = "ativoFilter";

    private static final PreLoadEventListener ENABLE_ATIVO_FILTER =
            (PreLoadEvent event) -> {
                if (event.getSession().getEnabledFilter(ATIVO_FILTER) == null) {
                    event.getSession()
                            .enableFilter(ATIVO_FILTER)
                            .setParameter("ativo", Boolean.TRUE);
                }
            };

    @Bean
    static BeanPostProcessor hibernateFilterActivationBpp() {
        return new HibernateFilterActivationBpp();
    }

    @Slf4j
    static class HibernateFilterActivationBpp implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName)
                throws BeansException {

            if (!(bean instanceof SessionFactory sessionFactory)) {
                return bean;
            }
            SessionFactoryImplementor impl =
                    sessionFactory.unwrap(SessionFactoryImplementor.class);

            EventListenerRegistry registry = requireNonNull(
                    impl.getServiceRegistry().getService(EventListenerRegistry.class),
                    "EventListenerRegistry not found");

            registry.getEventListenerGroup(EventType.PRE_LOAD)
                    .appendListener(ENABLE_ATIVO_FILTER);

            log.info("Filtro '{}' configurado para habilitação automática em PRE_LOAD", ATIVO_FILTER);
            return bean;
        }
    }
}
