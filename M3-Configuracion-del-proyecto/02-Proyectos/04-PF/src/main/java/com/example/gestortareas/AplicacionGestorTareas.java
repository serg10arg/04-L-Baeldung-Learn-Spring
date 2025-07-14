package com.example.gestortareas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AplicacionGestorTareas {

    private static final Logger LOG = LoggerFactory.getLogger(AplicacionGestorTareas.class);

    public static void main(String[] args) {

        // Ejecución con perfiles específicos, si se pasan como argumentos.
        // Ejemplo: java -jar tu-app.jar --spring.profiles.active=dev
        // O: java -Dspring.profiles.active=prod -jar tu-app.jar

        ConfigurableApplicationContext contexto = SpringApplication.run(AplicacionGestorTareas.class, args);
        LOG.info("Aplicacion 'Gestor de Tareas' iniciada");
        LOG.info("Perfiles activos: {}", String.join(", ", contexto.getEnvironment().getActiveProfiles()));
    }
}
