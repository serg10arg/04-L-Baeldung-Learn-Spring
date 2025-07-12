package com.example.p1;

import com.example.p1.modelo.Producto;
import com.example.p1.servicio.ServicioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; // Para ejecutar lógica al inicio de la app
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // Anotación principal de Spring Boot
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication // Indica que es una aplicación Spring Boot
public class EcommerceApp implements CommandLineRunner {

    private static final Logger REGISTRO = LoggerFactory.getLogger(EcommerceApp.class);

    @Autowired // Inyecta el ServicioProducto
    private ServicioProducto servicioProducto;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApp.class, args);
    }

    /**
     * Método que se ejecuta al inicio de la aplicación Spring Boot.
     * Simula la creación y guardado de productos.
     */
    @Override
    public void run(String... args) throws Exception {
        REGISTRO.info("--- Iniciando simulación de gestión de productos ---");

        // Ejemplo 1: Producto con stock suficiente
        Producto laptop = new Producto("Laptop Gamer 'Omega'", "Laptop de alto rendimiento para juegos", 25);
        servicioProducto.guardarProducto(laptop);
        REGISTRO.info("Resultado: {}", laptop);

        // Ejemplo 2: Producto con stock bajo
        Producto raton = new Producto("Ratón Ergonómico 'Precisión'", "Ratón inalámbrico con diseño ergonómico", 8);
        servicioProducto.guardarProducto(raton);
        REGISTRO.info("Resultado: {}", raton);

        // Ejemplo 3: Producto con stock crítico
        Producto teclado = new Producto("Teclado Mecánico 'Dominator'", "Teclado RGB retroiluminado", 3);
        servicioProducto.guardarProducto(teclado);
        REGISTRO.info("Resultado: {}", teclado);

        REGISTRO.info("--- Simulación de gestión de productos finalizada ---");
    }
}