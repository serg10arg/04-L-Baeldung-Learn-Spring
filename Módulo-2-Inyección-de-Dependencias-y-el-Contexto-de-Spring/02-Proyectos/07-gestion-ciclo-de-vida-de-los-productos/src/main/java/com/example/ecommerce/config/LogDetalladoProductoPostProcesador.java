package com.example.ecommerce.config;

import com.example.ecommerce.modelo.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * Otro post-procesador para demostrar el orden de ejecución de los post-procesadores
 * Su orden es mayor, por lo que se ejecutará después del Auditor General.
 */
public class LogDetalladoProductoPostProcesador implements BeanPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(LogDetalladoProductoPostProcesador.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.info("Log detallado 📊: Iniciando procesamiento para bean: '{}'", beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.info("Log Detallado 📊: Finalizado procesamiento para bean: '{}'", beanName);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        // Orden mayor (prioridad más baja). Se ejecuta después de AuditorProductoPostProcesador.
        return 20;
    }
}
