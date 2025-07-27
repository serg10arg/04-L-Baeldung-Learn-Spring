// Archivo: src/main/java/com/notificaciones/demo/NotificacionApplication.java
package com.example.notificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Clase principal de la aplicación Spring Boot para el Microservicio de Notificaciones.
 * Habilita el soporte para aplicaciones reactivas con WebFlux y repositorios reactivos de MongoDB.
 */
@SpringBootApplication //
@EnableWebFlux // Habilita la configuración de Spring WebFlux
@EnableReactiveMongoRepositories // Habilita los repositorios reactivos de Spring Data MongoDB
public class NotificacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificacionesApplication.class, args);
    }
}
