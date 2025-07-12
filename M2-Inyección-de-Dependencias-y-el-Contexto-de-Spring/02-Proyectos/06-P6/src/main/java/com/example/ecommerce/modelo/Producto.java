package com.example.ecommerce.modelo;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representa un producto en el sistema
 * Participa en el ciclo de vida de Spring para demostrar la personalización
 */
public class Producto {
    private static final Logger LOG = LoggerFactory.getLogger(Producto.class);

    private String id;
    private String nombre;
    private String sku; // Esta propiedad será modificada por el BeanFactortyPostProcessor

    public Producto() {
;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * Método de inicialización invocado por Spring después de que el bean ha sido construido
     * y todas sus propiedades han sido cargadas.
     */
    @PostConstruct // Este método se invoca después de la inyección de dependencias
    public void inicializarProducto() {
        LOG.info("➡️ Producto '{}' (ID: {}) ha sido inicializado. SKU actual: {}", this.nombre, this.id, this.sku);
    }
}
