package com.example.tiendaonline;

import com.example.tiendaonline.modelo.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.tiendaonline.modelo.Producto;
import com.example.tiendaonline.servicio.ServicioPedido;
import org.springframework.boot.SpringApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate; // Clase JdbcTemplate
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;


@SpringBootApplication
public class AplicacionTiendaOnline implements CommandLineRunner { // Implementamos CommandLineRunner

    private static final Logger LOG = LoggerFactory.getLogger(AplicacionTiendaOnline.class);

    @Autowired
    private JdbcTemplate plantillaJdbc; // Inyectamos JdbcTemplate

    @Autowired
    private ServicioPedido servicioPedido;

    public static void main(String[] args) {
        SpringApplication.run(AplicacionTiendaOnline.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("------ Iniciando configuración de base de datos con JdbcTemplate ------");
        // Crear una tabla de auditoría usando JdbcTemplate#execute para DDL
        plantillaJdbc.execute("CREATE TABLE auditoria (id INT AUTO_INCREMENT PRIMARY KEY, evento VARCHAR(255), fecha TIMESTAMP)");
        LOG.info("Tabla 'auditoria' creada con JdbcTemplate.");
        LOG.info("------ Fin de configuración de base de datos con JdbcTemplate ------");

        LOG.info("------ Demostrando creación de pedido con productos (transaccional) ------");

        // --- Caso de Éxito ---
        try {
            LOG.info("Creando Pedido EXITOSO...");
            // MEJORA: Usar BigDecimal para los precios para coincidir con el tipo de la entidad.
            Producto productoA = new Producto("Laptop", new BigDecimal("1200.00"), LocalDate.now());
            Producto productoB = new Producto("Ratón inalámbrico", new BigDecimal("25.00"), LocalDate.now());

            Pedido pedidoExitoso = servicioPedido.crearPedidoConProductos(
                    "Pedido de componentes de PC",
                    Arrays.asList(productoA, productoB)
            );
            LOG.info("Pedido exitoso creado: " + pedidoExitoso);
        } catch (Exception e) {
            LOG.error("Error inesperado al crear pedido exitoso: " + e.getMessage());
        }

        // --- Caso de Fallo para demostrar Rollback ---
        try {
            LOG.info("Creando Pedido con FALLO simulado (esperando rollback)...");
            // MEJORA: Usar BigDecimal para los precios.
            Producto productoC = new Producto("Teclado mecánico", new BigDecimal("75.00"), LocalDate.now());
            Producto productoFallo = new Producto("Monitor ERROR 27 pulgadas", new BigDecimal("300.00"), LocalDate.now()); // Este nombre activará el error simulado

            Pedido pedidoFallido = servicioPedido.crearPedidoConProductos(
                    "Pedido con error simulado",
                    Arrays.asList(productoC, productoFallo)
            );
            LOG.info("Pedido con fallo creado (NO DEBERÍA VERSE SI EL ROLLBACK FUNCIONÓ): " + pedidoFallido);
        } catch (Exception e) {
            LOG.error("Error capturado para pedido con fallo: " + e.getMessage());
            LOG.info("Verificar logs: Si el rollback funcionó, el pedido anterior y sus productos NO deberían aparecer en la DB.");
        }

        LOG.info("------ Verificando datos finales en la DB ------");
        // MEJORA: Usar Pageable para llamar a los métodos de servicio actualizados.
        Pageable primeraPagina = PageRequest.of(0, 10); // Solicitamos la primera página con hasta 10 elementos.
        LOG.info("Productos en la DB:");
        servicioPedido.obtenerTodosLosProductos(primeraPagina).forEach(producto -> LOG.info(producto.toString()));
        LOG.info("Pedidos en la DB:");
        servicioPedido.obtenerTodosLosPedidos(primeraPagina).forEach(pedido -> LOG.info(pedido.toString()));
    }
}
