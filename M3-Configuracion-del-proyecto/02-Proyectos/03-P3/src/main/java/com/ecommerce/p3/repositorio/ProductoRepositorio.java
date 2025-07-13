package com.ecommerce.p3.repositorio;

import com.ecommerce.p3.modelo.Producto;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.math.BigDecimal;

@Repository
public class ProductoRepositorio {
    // Este repositorio simula una base de dato

    // Simulación de guardado
    public Producto guardar (Producto producto) {
        // Lógica real de persistencia aquí
        // Asignamos un ID simulado para el ejemplo
        if (producto.getId() == null) {
            producto.setId(1L); // id fijo para simúlar uno nuevo
        }
        return producto;
    }

    // Simulación de búsqueda por ID
    public Optional<Producto> buscarPorId(Long id) {
        if (id == 1L) {
            // Retornamos un producto simulado si el ID es 1
            return Optional.of(new Producto(1L, "Producto 1", BigDecimal.valueOf(100)));
        }
        return Optional.empty(); // No se encontró el producto
    }
}
