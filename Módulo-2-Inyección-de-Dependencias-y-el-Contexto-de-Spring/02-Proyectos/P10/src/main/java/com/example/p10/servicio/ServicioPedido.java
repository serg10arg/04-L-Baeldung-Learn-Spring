package com.example.p10.servicio;

// INCORRECT: import com.example.p10.contratos.RepositorioPedido; (Wrong package)
// ANTI-PATTERN: import com.example.p10.repositorio.RepositorioPedidoImpl; (Should be the interface)
import com.example.p10.contratos.RepositorioPedido;
import com.example.p10.repositorio.RepositorioPedidoImpl; // CORRECT: Import the interface from the correct package.
import com.example.p10.excepcion.StockInsuficienteException;
import com.example.p10.modelo.Pedido;
import com.example.p10.modelo.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioPedido {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioPedido.class);
    private final RepositorioPedido repositorioPedido;
    private final ServicioProducto servicioProducto;
    private final ServicioPago servicioPago;
    private final ServicioGestionInventario servicioGestionInventario;

    public ServicioPedido(RepositorioPedido repositorioPedido,
                          ServicioProducto servicioProducto,
                          ServicioPago servicioPago,
                          ServicioGestionInventario servicioGestionInventario) {
        this.repositorioPedido = repositorioPedido;
        this.servicioProducto = servicioProducto;
        this.servicioPago = servicioPago;
        this.servicioGestionInventario = servicioGestionInventario;
        LOG.info("ServicioPedido inicializado y listo para operar.");
    }

    // ... rest of the class remains the same ...
    public Pedido crearPedido(List<Producto> productosSolicitados) {
        LOG.info("Iniciando creación de pedido con {} productos.", productosSolicitados.size());

        double total = calcularTotalYVerificarStock(productosSolicitados);

        Pedido nuevoPedido = new Pedido(null, productosSolicitados, total, "PENDIENTE");
        Pedido pedidoGuardado = repositorioPedido.guardar(nuevoPedido);

        LOG.info("Pedido {} creado exitosamente.", pedidoGuardado.getId());
        return pedidoGuardado;
    }

    private double calcularTotalYVerificarStock(List<Producto> productosSolicitados) {
        double total = 0.0;
        for (Producto pSolicitado : productosSolicitados) {
            Producto pEnStock = servicioProducto.obtenerProductoPorId(pSolicitado.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + pSolicitado.getId()));

            if (pEnStock.getStock() < pSolicitado.getStock()) {
                throw new StockInsuficienteException("Stock insuficiente para " + pEnStock.getNombre() +
                        ". Solicitado: " + pSolicitado.getStock() + ", Disponible: " + pEnStock.getStock());
            }

            total += pEnStock.getPrecio() * pSolicitado.getStock();
            servicioGestionInventario.verificarInventario(pEnStock.getId());
            servicioProducto.reducirStock(pEnStock.getId(), pSolicitado.getStock());
        }
        return total;
    }

    public boolean procesarPagoPedido(Long idPedido) {
        LOG.info("Intentando procesar pago para el pedido {}", idPedido);
        Optional<Pedido> pedidoOpt = repositorioPedido.buscarPorId(idPedido);

        if (pedidoOpt.isEmpty()) {
            LOG.error("Intento de pago para pedido no existente: {}", idPedido);
            return false;
        }

        Pedido pedido = pedidoOpt.get();

        if (!"PENDIENTE".equals(pedido.getEstado())) {
            LOG.warn("El pedido {} no está PENDIENTE, estado actual: {}. No se puede procesar el pago.", idPedido, pedido.getEstado());
            return false;
        }

        boolean pagoExitoso = servicioPago.procesar(pedido.getTotal());
        if (pagoExitoso) {
            pedido.setEstado("PAGADO");
            repositorioPedido.guardar(pedido);
            LOG.info("Pago del pedido {} procesado con éxito.", idPedido);
            return true;
        } else {
            LOG.error("Fallo al procesar el pago del pedido {}.", idPedido);
            pedido.setEstado("PAGO_FALLIDO");
            repositorioPedido.guardar(pedido);
            return false;
        }
    }

    public Optional<Pedido> obtenerPedido(Long idPedido) {
        return repositorioPedido.buscarPorId(idPedido);
    }
}