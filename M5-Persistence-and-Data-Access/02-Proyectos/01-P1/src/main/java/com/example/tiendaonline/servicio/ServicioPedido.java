package com.example.tiendaonline.servicio;

import com.example.tiendaonline.modelo.Pedido;
import com.example.tiendaonline.modelo.Producto;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.tiendaonline.repositorio.IPedidoRepositorio;
import com.example.tiendaonline.repositorio.IProductoRepositorio;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioPedido {

    private final IPedidoRepositorio pedidoRepositorio;
    private final IProductoRepositorio productoRepositorio;

    public ServicioPedido(IPedidoRepositorio pedidoRepositorio, IProductoRepositorio productoRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    // @Transactional asegura que si algo falla, todo el proceso se revierta.
    // rollbackFor = Exception.class asegura rollback para cualquier tipo de excepción.
    @Transactional(rollbackFor = Exception.class)
    public Pedido crearPedidoConProductos(String descripcionPedido, List<Producto> productos) {
        // 1. Crear el objeto Pedido en memoria.
        Pedido nuevoPedido = new Pedido(descripcionPedido, LocalDate.now());

        // 2. Construir el grafo de objetos en memoria.
        //    Asociar cada producto con el pedido usando el método de ayuda.
        for (Producto producto : productos) {
            // Simulamos un fallo para demostrar el rollback de la transacción
            if (producto.getNombre().contains("ERROR")) {
                throw new RuntimeException("Error simulado al guardar producto: " + producto.getNombre()); // Esto causara el rollback
            }
            // El método de ayuda establece la relación bidireccional correctamente.
            nuevoPedido.addProducto(producto);
        }

        // 3. Guardar el agregado raíz (Pedido) UNA SOLA VEZ.
        //    Gracias a `cascade = CascadeType.ALL` en la entidad Pedido, JPA detectará
        //    los nuevos productos en la colección y los guardará automáticamente.
        //    Esto reduce drásticamente las llamadas a la base de datos.
        return pedidoRepositorio.save(nuevoPedido);
    }

    // Usar Paginación para evitar cargar todos los datos y prevenir OutOfMemoryError.
    public Page<Pedido> obtenerTodosLosPedidos(Pageable pageable) {
        return  pedidoRepositorio.findAll(pageable);
    }

    // Usar Paginación para evitar cargar todos los datos y prevenir OutOfMemoryError.
    public Page<Producto> obtenerTodosLosProductos(Pageable pageable) {
        return  productoRepositorio.findAll(pageable);
    }

}
