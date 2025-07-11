package com.example.p10.servicio;

import com.example.p10.contratos.RepositorioProducto;
import com.example.p10.modelo.Producto;
import com.example.p10.repositorio.RepositorioProductoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing product-related business logic.
 * It is annotated with @Service to be managed by Spring's IoC container.
 */
@Service
public class ServicioProducto {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioProducto.class);
    private final RepositorioProducto repositorioProducto;

    /**
     * Constructor-based dependency injection.
     * Spring will automatically inject the RepositorioProductoImpl bean.
     *
     * @param repositorioProducto The repository for product data access.
     */
    public ServicioProducto(RepositorioProducto repositorioProducto) {
        this.repositorioProducto = repositorioProducto;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to find.
     * @return an Optional containing the product if found, or empty otherwise.
     */
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return repositorioProducto.buscarPorId(id);
    }

    /**
     * Reduces the stock for a given product.
     *
     * @param idProducto The ID of the product to update.
     * @param cantidad   The quantity to reduce from the stock.
     */
    public void reducirStock(Long idProducto, int cantidad) {
        repositorioProducto.buscarPorId(idProducto).ifPresent(producto -> {
            if (producto.getStock() >= cantidad) {
                int nuevoStock = producto.getStock() - cantidad;
                repositorioProducto.actualizarStock(idProducto, nuevoStock);
            } else {
                // In a real application, this would throw a specific exception.
                LOG.error("Attempted to reduce stock for '{}' but stock is insufficient.", producto.getNombre());
            }
        });
    }
}