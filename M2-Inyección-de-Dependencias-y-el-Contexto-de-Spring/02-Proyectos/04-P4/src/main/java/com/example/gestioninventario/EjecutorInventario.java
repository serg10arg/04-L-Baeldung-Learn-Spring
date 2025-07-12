package com.example.gestioninventario;


import com.example.gestioninventario.contratos.ServicioProducto;
import com.example.gestioninventario.producto.ServicioProductoImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EjecutorInventario implements CommandLineRunner {

    private final ServicioProducto servicioProducto;

    public EjecutorInventario(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- REALIZANDO OPERACIONES DE INVENTARIO ---");

        String resultado1 = servicioProducto.agregarProducto("Laptop Dell XPS 15");
        System.out.println(resultado1);

        String resultado2 = servicioProducto.obtenerProducto("ID-12345");
        System.out.println(resultado2);

        System.out.println("--- FIN DE OPERACIONES ---\n");
    }
}