package com.example;

import com.example.p.servicio.ServicioGestionPedidos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EjecutorDeDemostracion implements CommandLineRunner {

    private final ServicioGestionPedidos servicioPedidos;

    // MEJORA: Se inyecta el servicio por constructor.
    public EjecutorDeDemostracion(ServicioGestionPedidos servicioPedidos) {
        this.servicioPedidos = servicioPedidos;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- Ejecutando Demostración ---");
        servicioPedidos.mostrarConfiguracionPedidos();
        servicioPedidos.procesarPedido("PEDIDO-001");
        servicioPedidos.procesarPedido("PEDIDO-002");
        System.out.println("--- Demostración Finalizada ---\n");
    }
}