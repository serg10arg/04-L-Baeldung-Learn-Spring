package com.example.proyectos;

import com.example.proyectos.entidades.EstadoTarea;
import com.example.proyectos.entidades.Proyecto;
import com.example.proyectos.entidades.Tarea;
import com.example.proyectos.servicios.IProjectService;
import com.example.proyectos.servicios.ITaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@SpringBootApplication
public class GestorProyectosApplication implements CommandLineRunner { // Implementar CommandLineRunner

    private static final Logger LOG = LoggerFactory.getLogger(GestorProyectosApplication.class);

    // MEJORA: Inyección por constructor. Es más robusto y facilita las pruebas.
    private final IProjectService projectService;
    private final ITaskService taskService;

    public GestorProyectosApplication(IProjectService projectService, ITaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GestorProyectosApplication.class, args);
    }

    /**
     * El método run de CommandLineRunner es el lugar ideal para ejecutar lógica de demostración
     * después de que el contexto de Spring se haya cargado por completo.
     */
    @Override
    public void run(String... args) throws Exception {
        LOG.info("------ INICIANDO DEMOSTRACIÓN DE LA CAPA DE SERVICIO ------");

        // 1. Crear un proyecto con tareas para demostrar la persistencia en cascada
        Proyecto nuevoProyecto = new Proyecto("Desarrollo App Web", "Proyecto para cliente final", LocalDate.now());

        Tarea tarea1 = new Tarea();
        tarea1.setNombre("Diseño de la UI/UX");
        tarea1.setEstado(EstadoTarea.PENDIENTE);

        Tarea tarea2 = new Tarea();
        tarea2.setNombre("Desarrollo del Backend API");
        tarea2.setEstado(EstadoTarea.EN_PROGRESO);

        // Usamos los métodos de ayuda para mantener la consistencia
        nuevoProyecto.addTarea(tarea1);
        nuevoProyecto.addTarea(tarea2);

        Proyecto proyectoGuardado = projectService.crearProyecto(nuevoProyecto);
        LOG.info("PROYECTO GUARDADO CON ÉXITO: {}", proyectoGuardado);

        // 2. Demostrar la recuperación paginada de datos
        Pageable primeraPagina = PageRequest.of(0, 5); // Solicitar la primera página con 5 elementos

        LOG.info("--- Recuperando todos los proyectos (Paginado) ---");
        // CORRECCIÓN: No se puede llamar a toString() en un objeto con colecciones LAZY fuera de una transacción.
        // En un caso real, se procesarían los datos o se pasarían a una capa de DTO.
        // Para la demo, simplemente mostraremos que la paginación funciona.
        projectService.findAll(primeraPagina).forEach(p -> LOG.info("Proyecto encontrado: " + p.getNombre()));

        LOG.info("--- Recuperando todas las tareas (Paginado) ---");
        taskService.findAll(primeraPagina)
                .forEach(t -> LOG.info("Tarea encontrada: " + t.getNombre()));

        // 3. Demostrar la carga optimizada con JOIN FETCH
        LOG.info("--- Recuperando proyecto ID {} con sus tareas (JOIN FETCH) ---", proyectoGuardado.getId());
        projectService.findByIdWithTasks(proyectoGuardado.getId())
                .ifPresent(p -> LOG.info("Proyecto encontrado: " + p));

        LOG.info("------ FIN DE LA DEMOSTRACIÓN ------");
    }
}
