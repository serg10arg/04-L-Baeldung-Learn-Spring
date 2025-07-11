package com.example.p10;


import com.example.p10.excepcion.StockInsuficienteException;
import com.example.p10.modelo.Pedido;
import com.example.p10.modelo.Producto;
import com.example.p10.servicio.ServicioPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;
import java.util.Optional;

// @SpringBootApplication ya incluye @ComponentScan para este paquete y sus subpaquetes.
// No es necesario añadir @ComponentScan explícitamente si se sigue la estructura estándar.
@SpringBootApplication
public class AplicacionPrincipal implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AplicacionPrincipal.class);
    private final ServicioPedido servicioPedido;

    // Inyección por constructor, la forma recomendada para beans de aplicación.
    public AplicacionPrincipal(ServicioPedido servicioPedido) {
        this.servicioPedido = servicioPedido;
    }

    public static void main(String[] args) {
        SpringApplication.run(AplicacionPrincipal.class, args);
    }

    @Override
    public void run(String... args) {
        LOG.info("--- Ejecutando demostración del Sistema de Gestión de Pedidos ---");

        // Productos que el cliente quiere comprar
        Producto laptop = new Producto(1L, null, 0, 1); // Solo necesitamos ID y cantidad
        Producto raton = new Producto(2L, null, 0, 2);

        // 1. Crear un pedido
        try {
            LOG.info("\n--- 1. Creando un nuevo pedido ---");
            Pedido pedido1 = servicioPedido.crearPedido(List.of(laptop, raton));
            LOG.info("Pedido creado: ID {}, Estado: {}, Total: ${}", pedido1.getId(), pedido1.getEstado(), pedido1.getTotal());

            // 2. Procesar el pago del pedido
            LOG.info("\n--- 2. Procesando pago del pedido {} ---", pedido1.getId());
            boolean pagoExitoso = servicioPedido.procesarPagoPedido(pedido1.getId());
            if (pagoExitoso) {
                Optional<Pedido> pedidoActualizado = servicioPedido.obtenerPedido(pedido1.getId());
                pedidoActualizado.ifPresent(p -> LOG.info("Estado del pedido después del pago: {}", p.getEstado()));
            }

            // 3. Simular otro pedido para ver el stock
            LOG.info("\n--- 3. Intentando crear otro pedido con stock limitado ---");
            Producto otraLaptop = new Producto(1L, null, 0, 10); // Intentamos pedir 10 laptops
            servicioPedido.crearPedido(List.of(otraLaptop));

        } catch (StockInsuficienteException e) {
            LOG.error("ERROR CONTROLADO: {}", e.getMessage());
        } catch (Exception e) {
            LOG.error("Ocurrió un error inesperado durante la demostración:", e);
        }

        LOG.info("\n--- Demostración finalizada ---");
    }
}