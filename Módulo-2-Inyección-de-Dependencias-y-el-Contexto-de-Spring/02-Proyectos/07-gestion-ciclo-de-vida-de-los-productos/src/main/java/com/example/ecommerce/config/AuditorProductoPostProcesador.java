package com.example.ecommerce.config;

import com.example.ecommerce.modelo.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;


/**
 * Intercepta cada bean de tipo Producto DESPUÉS de su instanciación para realizar una auditoria.
 */
public class AuditorProductoPostProcesador implements BeanPostProcessor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(AuditorProductoPostProcesador.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Producto) {
            LOG.info("Auditor General 🕵️: Antes de inicializar el bean: '{}' (ID: {})", beanName, ((Producto) bean).getId());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof  Producto) {
            LOG.info("Auditor General 🕵️: Después de inicializar el bean: '{}' (ID: {})", beanName, ((Producto) bean).getId());
        }
        return bean;
    }

    @Override
    public int getOrder() {
        // Order intermedio. Se ejecutará antes que cualquier post-procesador con un número mayor.
        return 10;
    }
}
