package com.example.gestionproyectos;

import com.example.gestionproyectos.modelos.EstadoTarea;
import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.Tarea;
import com.example.gestionproyectos.repositorios.RepositorioProyecto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile; // <-- IMPORTAR ESTA CLASE

import java.time.LocalDate;

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Bean para inicializar datos de prueba.
     * ANÁLISIS SENIOR: Se activa solo con el perfil "dev" para que no interfiera con las pruebas.
     */
    @Bean
    @Profile("dev") // <-- AÑADIR ESTA LÍNEA
    public CommandLineRunner inicializarDatos(RepositorioProyecto repositorioProyecto) {
        return args -> {
            // ... el resto de tu código se mantiene igual
            LOG.info("Iniciando carga de datos de ejemplo...");

            // --- Creación del Proyecto 1 y sus Tareas (Forma Profesional) ---
            Proyecto proyecto1 = new Proyecto("Desarrollo de Aplicación Web", LocalDate.now());

            proyecto1.addTarea(new Tarea(null, "Diseñar interfaz de usuario", "Crear mockups y prototipos para la UI.", EstadoTarea.PENDIENTE, null));
            proyecto1.addTarea(new Tarea(null, "Implementar backend API", "Desarrollar los endpoints REST para la aplicación.", EstadoTarea.EN_PROGRESO, null));
            proyecto1.addTarea(new Tarea(null, "Configurar base de datos", "Establecer el esquema de la base de datos y la conectividad.", EstadoTarea.COMPLETADA, null));


            // --- Creación del Proyecto 2 y sus Tareas (Forma Profesional) ---
            Proyecto proyecto2 = new Proyecto("Migración a la Nube", LocalDate.now().minusMonths(3));
            proyecto2.addTarea(new Tarea(null, "Evaluar proveedores de nube", "Investigar y comparar opciones de proveedores de servicios en la nube.", EstadoTarea.COMPLETADA, null));
            proyecto2.addTarea(new Tarea(null, "Planificar estrategia de migración", "Definir un plan detallado para la migración.", EstadoTarea.PENDIENTE, null));

            // --- Proyecto sin tareas ---
            Proyecto proyecto3 = new Proyecto("Optimización de Base de Datos", LocalDate.now().plusMonths(1));


            repositorioProyecto.save(proyecto1);
            repositorioProyecto.save(proyecto2);
            repositorioProyecto.save(proyecto3);

            LOG.info("Datos de ejemplo cargados exitosamente.");

            LOG.info("Proyectos existentes en la base de datos:");
            repositorioProyecto.findAll().forEach(proyecto -> {
                LOG.info("Proyecto: id={}, nombre='{}'", proyecto.getId(), proyecto.getNombre());
                // La carga de tareas es LAZY, se cargarán aquí al ser accedidas
                // NOTA: Este bloque puede causar LazyInitializationException si no se maneja en una transacción.
                // Para el seeder está bien, pero en las pruebas se debe evitar.
                // Con @Profile("dev") este bloque ya no se ejecutará en los tests.
            });
        };
    }
}