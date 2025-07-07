package com.example.pedidosonline.interfaces;

import com.example.pedidosonline.modelo.Producto;

import java.util.List;
import java.util.Optional;

public interface ServicioProducto {
    List<Producto> obtenerTodosLosProductos();
    Optional<Producto> obtenerProductoPorId(String id);
    Producto guardarProducto(Producto producto);
}
