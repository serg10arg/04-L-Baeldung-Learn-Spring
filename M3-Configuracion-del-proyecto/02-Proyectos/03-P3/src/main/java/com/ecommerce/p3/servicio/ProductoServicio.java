package com.ecommerce.p3.servicio;

import com.ecommerce.p3.modelo.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ecommerce.p3.repositorio.ProductoRepositorio;

import java.util.Optional;

@Service
public class ProductoServicio {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoServicio.class);
    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public Producto guardarProducto(Producto producto) {
        // Log a nivel DEBUG para ver el inicio de la operación (Concepto 3)
        // Uso de placeholders para evitar concatenación de cadenas (Concepto 2)
        LOG.debug("Iniciando el proceso de guardado para el producto {}: ", producto.getNombre());

        try {
            Producto productoGuardado = productoRepositorio.guardar(producto);
            // Log a nivel INFO para un evento significativo (Concepto 3)
            LOG.info("Producto guardado exitosamente con ID {}: ", productoGuardado.getId());
            return  productoGuardado;
        } catch (Exception e) {
            LOG.error("Error al intentar guardar el producto con nombre '{}'", producto.getNombre(), e);
            throw  new RuntimeException("No se pudo guardar el producto", e);
        }
    }

    public Optional<Producto> buscarPorId(Long id) {
        // Log a nivel INFO para un resultado positivo (Concepto 3)
        LOG.debug("Buscando producto con id: {}", id);

        Optional<Producto> productoEncontrado = productoRepositorio.buscarPorId(id);

        if (productoEncontrado.isPresent()) {
            // Log a nivel INFO para un resultado positivo (Concepto 3)
            LOG.info("Producto encontrado con ID {}: ", id);
        } else {
            // Log a nivel WARN para una situación que podría ser problemática pero no fatal (Concepto 3)
            LOG.warn("No se encontró ningún producto con ID {}: ", id);
        }
        return productoEncontrado;
    }
}
