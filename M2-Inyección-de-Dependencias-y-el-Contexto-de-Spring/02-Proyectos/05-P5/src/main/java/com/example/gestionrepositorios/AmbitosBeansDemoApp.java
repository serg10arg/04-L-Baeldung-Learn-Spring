package com.example.gestionrepositorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmbitosBeansDemoApp {

    public static void main(String[] args) {
        // El método main se mantiene limpio. Su única responsabilidad es arrancar la aplicación.
        // Spring escaneará los componentes (@Service, @Configuration, etc.), los creará
        // y ejecutará automáticamente el método @PostConstruct en ServicioProyectoImpl.
        SpringApplication.run(AmbitosBeansDemoApp.class, args);
    }

}
