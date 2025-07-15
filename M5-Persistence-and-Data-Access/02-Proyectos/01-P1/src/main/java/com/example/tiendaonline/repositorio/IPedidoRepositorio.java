package com.example.tiendaonline.repositorio;

import com.example.tiendaonline.modelo.Pedido;
import org.springframework.data.repository.CrudRepository;

public interface IPedidoRepositorio extends CrudRepository<Pedido, Long> {
}
