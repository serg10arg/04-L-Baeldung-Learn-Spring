package com.example.gestioninventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionInventarioApp {

    public static void main(String[] args) {
        // El método main se mantiene limpio, su única responsabilidad es arrancar la aplicación.
        // Spring se encargará de encontrar y ejecutar el CommandLineRunner (EjecutorInventario).
        SpringApplication.run(GestionInventarioApp.class, args);
    }

}
