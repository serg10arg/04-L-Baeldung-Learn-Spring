package com.example.p10.contratos;

import com.example.p10.modelo.Pedido;

import java.util.Optional;

public interface RepositorioPedido {
    Optional<Pedido> buscarPorId(Long id);
    Pedido guardar(Pedido pedido);
}
