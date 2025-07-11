package com.example.p10.repositorio;

import com.example.p10.contratos.RepositorioPedido;
import com.example.p10.modelo.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de pedidos.
 * Anotado con @Repository para que Spring lo detecte como un bean.
 */
@Repository
public class RepositorioPedidoImpl implements RepositorioPedido {

    private static final Logger LOG = LoggerFactory.getLogger(RepositorioPedidoImpl.class);
    private final Map<Long, Pedido> pedidosDB = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        LOG.debug("Buscando pedido con ID: {}", id);
        return Optional.ofNullable(pedidosDB.get(id));
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        // Asigna un nuevo ID si el pedido es nuevo
        if (pedido.getId() == null) {
            pedido.setId(nextId++);
        }
        LOG.info("Guardando pedido con ID: {}", pedido.getId());
        pedidosDB.put(pedido.getId(), pedido);
        return pedido;
    }
}