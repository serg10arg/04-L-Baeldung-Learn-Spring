package com.example.ecommerce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * Intercepta la configuración de beans ANTES de su instanciación para modificar
 * la definición del bean 'productoPrincipal' y asignarle un SKU por defecto.
 */
public class ConfiguradorSkuProductoFactory implements BeanFactoryPostProcessor, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguradorSkuProductoFactory.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LOG.info("⚙️ Ejecutando ConfiguradorSkuProductoFactory (BeanFactoryPostProcessor). " +
                "Modificando BeanDefinition antes de la instanciación.");

        final String nombreBeanObjetivo = "productoPrincipal";

        if (beanFactory.containsBeanDefinition(nombreBeanObjetivo)) {
            BeanDefinition definicionBean = beanFactory.getBeanDefinition(nombreBeanObjetivo);
            definicionBean.getPropertyValues().add("sku", "DEFAULT-SKU-ECOMMERCE");
            LOG.info("✅ Propiedad 'sku' de '{}' modificada a 'DEFAULT-SKU-ECOMMERCE'.", nombreBeanObjetivo);
        } else {
            LOG.warn("⚠️ BeanDefinition para '{}' no encontrada. La configuración de SKU no se aplicará.",
                    nombreBeanObjetivo);
        }
    }

    @Override
    public int getOrder() {
        // Prioridad alta (valor bajo) para asegurar que se ejecute primero.
        return 1;
    }
}