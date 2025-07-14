package com.example.gestiontareas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemaDeTareasAplicacion implements CommandLineRunner {

    private static final Logger REGISTRADOR = LoggerFactory.getLogger(SistemaDeTareasAplicacion.class);

    public static void main(String... args) {
        REGISTRADOR.info("INICIANDO LA APLICACION DEL SISTEMA DE TAREAS");
        SpringApplication.run(SistemaDeTareasAplicacion.class, args);
        REGISTRADOR.info("ARRANQUE DE LA APLICACION FINALIZADO");
    }

    @Override
    public void run(String... args) throws Exception {
        // Pila 1 (Actuadores), Pila 2 (Salud), Pila 3 (Info), Pila 5 (Configuracion Actuadores)
        // La aplicacion ha arrancado. Puedes acceder a:
        // - Salud: http://localhost:8080/monitoreo/health
        // - Informacion: http://localhost:8080/monitoreo/informacion
        // - Loggers: http://localhost:8080/monitoreo/loggers
        REGISTRADOR.info("La aplicacion Sistema de Tareas ha arrancado y esta lista para operar");
    }

    @Bean
    public ObjectMapper miObjectMapperPersonalizado() {
        REGISTRADOR.info(">>> Pila 4: Se esta creando un ObjectMapper personalizado. La auto-conf");
        return new ObjectMapper();
    }
}
