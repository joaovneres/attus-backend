package br.com.attus.processos.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreLoadEventListener;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HibernateFilterConfigurationTest {

    @Test
    @DisplayName("BeanPostProcessor registra listener de PRE_LOAD quando bean é SessionFactory")
    @SuppressWarnings("unchecked")
    void shouldRegisterPreLoadListener() {
        SessionFactory sessionFactory = mock(SessionFactory.class);
        SessionFactoryImplementor impl = mock(SessionFactoryImplementor.class);
        when(sessionFactory.unwrap(SessionFactoryImplementor.class)).thenReturn(impl);

        org.hibernate.service.ServiceRegistry svcRegistry = mock(org.hibernate.service.ServiceRegistry.class);
        when(impl.getServiceRegistry()).thenReturn((ServiceRegistryImplementor) svcRegistry);

        EventListenerRegistry listenerRegistry = mock(EventListenerRegistry.class);
        when(svcRegistry.getService(EventListenerRegistry.class)).thenReturn(listenerRegistry);

        EventListenerGroup<PreLoadEventListener> group = mock(EventListenerGroup.class);
        when(listenerRegistry.getEventListenerGroup(EventType.PRE_LOAD)).thenReturn(group);

        var bpp = new HibernateFilterConfiguration.HibernateFilterActivationBpp();
        bpp.postProcessAfterInitialization(sessionFactory, "sessionFactory");

        verify(group).appendListener(any(PreLoadEventListener.class));
    }

    @Test
    @DisplayName("BeanPostProcessor ignora beans que não sejam SessionFactory")
    void shouldIgnoreOtherBeans() {
        var otherBean = new Object();
        var bpp = new HibernateFilterConfiguration.HibernateFilterActivationBpp();

        Object result = bpp.postProcessAfterInitialization(otherBean, "any");

        assertThat(result).isSameAs(otherBean);
    }
}