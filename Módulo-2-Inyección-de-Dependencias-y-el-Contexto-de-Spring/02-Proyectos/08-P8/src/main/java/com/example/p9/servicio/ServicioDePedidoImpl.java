package com.example.p9.servicio;

import com.example.p9.interfaz.IRepositorioDeProducto;
import com.example.p9.interfaz.IServicioDeInventario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.p9.interfaz.IServicioDePedido;

import javax.inject.Inject;
import javax.inject.Named;

@Service
public class ServicioDePedidoImpl implements  IServicioDePedido {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioDePedidoImpl.class);

    // 1. Dependencias declaradas como FINAL para garantizar inmutabilidad.
    private final IServicioDeInventario servicioInventario;
    private final IRepositorioDeProducto repositorioProducto;

    @Inject
    public ServicioDePedidoImpl(IServicioDeInventario servicioInventario,
                                @Named("repositorioProductoPrincipal") IRepositorioDeProducto repositorioProducto) {

        this.servicioInventario = servicioInventario;
        this.repositorioProducto = repositorioProducto;

        LOG.info("Cableado: IServicioDeInventario inyectado via constructor. Instancia: {}",
                servicioInventario.getClass().getSimpleName());

        LOG.info("Cableado: IRepositorioDeProducto inyectado via constructor con @Named. Instancia: {}",
                repositorioProducto.getClass().getSimpleName());
    }

    @Override
    public void procesarNuevoPedido(String producto, int cantidad) {
        LOG.info("\n--- Procesando Nuevo Pedido ---");
        LOG.info("Producto: {}, Cantidad {}", producto, cantidad);

        repositorioProducto.guardarProducto(producto);
        servicioInventario.actualizarStock(producto, cantidad);

        LOG.info("Pedido para '{}' procesado exitosamente.", producto);
        LOG.info("--------------------------------");

    }
}
