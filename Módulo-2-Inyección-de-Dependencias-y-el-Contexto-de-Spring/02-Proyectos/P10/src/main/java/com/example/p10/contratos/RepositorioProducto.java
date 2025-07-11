package com.example.p10.contratos;

import com.example.p10.modelo.Producto;

import java.util.Optional;

public interface RepositorioProducto {
    Optional<Producto> buscarPorId(Long id);
    Producto guardar(Producto producto);
    void actualizarStock(Long id, int cantidad);
}
