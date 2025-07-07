package com.example.tiendaonline;

import com.example.tiendaonline.pedido.ServicioPedidos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EjecutorTienda implements CommandLineRunner {

    private final ServicioPedidos servicioPedidos;

    public EjecutorTienda(ServicioPedidos servicioPedidos) {
        this.servicioPedidos = servicioPedidos;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- FASE DE USO: Simulando operaciones de la tienda... ---");
        servicioPedidos.realizarPedidos("Laptop Gamer", 1 );
        servicioPedidos.realizarPedidos("Teclado Mecanico", 2);
        servicioPedidos.realizarPedidos("Monitor Curvo", 1); // Producto sin stock
        System.out.println("--- Fin de la simulacion de uso. ---");
    }
}
