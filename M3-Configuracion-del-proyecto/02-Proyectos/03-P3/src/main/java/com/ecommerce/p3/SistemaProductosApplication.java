package com.ecommerce.p3;

import com.ecommerce.p3.modelo.Producto;
import com.ecommerce.p3.servicio.ProductoServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class SistemaProductosApplication {

    public static void main(String[] args) {

        SpringApplication.run(SistemaProductosApplication.class, args);
    }

    // Este @Bean se ejecutará una vez que la aplicación se inicie.
    // Lo usamos para simular operaciones y ver los logs en acción.
    @Bean
    public CommandLineRunner ejecutarOperacionesSimuladas(ProductoServicio servicio) {
        return args -> {
            System.out.println("Iniciando operaciones simuladas");

            // 1. Guardar un producto (activará logs INFO y DEBUG en nuestro paquete)
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombre("Monitor UltraWide");
            nuevoProducto.setPrecio(BigDecimal.valueOf(499.99));
            servicio.guardarProducto(nuevoProducto);

            // 2. Buscar un producto existente (activará logs INFO y DEBUG)
            servicio.buscarPorId(1L);

            // 3. Buscar un producto no existente (activará logs WARN y DEBUG)
            servicio.buscarPorId(99L);

            System.out.println("--- Operaciones de prueba finalizadas ---\n");
            // Puedes revisar la consola y el archivo logs/sistema-productos.log para ver los logs.
        };

    }

}
