package com.example.pedidosonline;

import com.example.pedidosonline.interfaces.ServicioProducto;
import com.example.pedidosonline.modelo.Producto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EjecutorAplicacion implements CommandLineRunner {

    private final ServicioProducto servicioProducto;

    public EjecutorAplicacion(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- EJECUTANDO LÓGICA DE ARRANQUE ---");

        System.out.println("1. Listando productos iniciales:");
        servicioProducto.obtenerTodosLosProductos().forEach(p ->
                System.out.println("- " + p.getNombre() + " ($" + p.getPrecio() + ")")
        );

        System.out.println("\n2. Añadiendo un nuevo producto...");
        // Usamos UUID para generar un ID unico para el nuevo producto
        Producto nuevoProducto = new Producto(UUID.randomUUID().toString(), "Monitor 4k", 350.00);
        servicioProducto.guardarProducto(nuevoProducto);

        System.out.println("\n3. Listando productos actualizados:");
        servicioProducto.obtenerTodosLosProductos().forEach(p ->
                System.out.println("- " + p.getNombre() + " ($" + p.getPrecio() + ")")
        );

        System.out.println("\n--- LÓGICA DE ARRANQUE FINALIZADA ---\n");
    }
}
