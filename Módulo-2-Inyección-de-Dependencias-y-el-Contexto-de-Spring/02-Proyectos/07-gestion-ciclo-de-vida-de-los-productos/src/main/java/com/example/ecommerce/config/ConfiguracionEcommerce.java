package com.example.ecommerce.config;

import com.example.ecommerce.modelo.Producto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración principal Spring.
 * Define los beans de la aplicación y registra los post-procesadores.
 */
@Configuration
public class ConfiguracionEcommerce {

    @Bean
    public Producto productoPrincipal() {
        Producto producto = new Producto();
        producto.setId("PROD-001");
        producto.setNombre("Laptop Pro");
        // El 'sku' será establecido por el ConfiguradorSkuProductoFactory
        return producto;
    }

    @Bean
    public Producto accesorioProducto() {
        Producto producto = new Producto();
        producto.setId("ACC-001");
        producto.setNombre("Mouse Inalámbrico");
        // Este bean será afecado por el ConfiguradorSkuProductoFactory
        return producto;
    }

    @Bean
    public static ConfiguradorSkuProductoFactory configuradorSkuProductoFactory() {
        return new ConfiguradorSkuProductoFactory();
    }

    @Bean
    public static AuditorProductoPostProcesador auditorProductoPostProcesador() {
        return new AuditorProductoPostProcesador();
    }

    @Bean
    public static LogDetalladoProductoPostProcesador logDetalladoProductoPostProcesador() {
        return new LogDetalladoProductoPostProcesador();
    }
}
