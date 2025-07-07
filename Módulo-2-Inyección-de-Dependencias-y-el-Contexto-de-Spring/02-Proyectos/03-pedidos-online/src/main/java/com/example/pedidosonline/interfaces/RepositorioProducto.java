package com.example.pedidosonline.interfaces;

import com.example.pedidosonline.modelo.Producto;

import java.util.List;
import java.util.Optional;

public interface RepositorioProducto {
    List<Producto> buscarTodos();
    Optional<Producto> buscarPorId(String id);
    Producto guardar(Producto producto);
}
