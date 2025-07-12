package com.example.p10.repositorio;

import com.example.p10.contratos.RepositorioProducto;
import com.example.p10.modelo.Producto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RepositorioProductoImpl implements RepositorioProducto {

    private static final Logger LOG = LoggerFactory.getLogger(RepositorioProductoImpl.class);
    private final Map<Long, Producto> productosDB = new HashMap<>();

    @PostConstruct
    public void initDB() {
        productosDB.put(1L, new Producto(1L, "Laptop Gamer Pro", 1200.00, 10));
        productosDB.put(2L, new Producto(2L, "Mouse Optico", 25.00, 50));
        LOG.info("Base de datos de productos en memoria inicializada con dos productos");
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        LOG.debug("Buscando producto con ID {}", id);
        return Optional.ofNullable(productosDB.get(id));
    }

    @Override
    public Producto guardar(Producto producto) {
        LOG.info("Guardando producto con ID {}", producto.getNombre());
        productosDB.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public void actualizarStock(Long id, int nuevoStock) {
        Producto producto =  productosDB.get(id);
        if (producto != null) {
            LOG.info("Actualizando stock para '{}': de {} a {}", producto.getNombre(), producto.getStock(), nuevoStock);
            producto.setStock(nuevoStock);
        }
    }
}
