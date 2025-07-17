package com.example.proyectos.servicios;

import com.example.proyectos.entidades.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProjectService {

    /**
     * Guarda un nuevo proyecto en la base de datos.
     * Si el proyecto contiene tareas, estas se guardarán en cascada.
     * @param proyecto El objeto Proyecto a persistir.
     * @return El proyecto guardado con su ID asignado.
     */
    Proyecto crearProyecto(Proyecto proyecto);

    /**
     * Recupera todos los proyectos de forma paginada para evitar problemas de rendimiento.
     * @param pageable Objeto que contiene la información de paginación (número de página, tamaño, orden).
     * @return una `Page` de proyectos.
     */
    Page<Proyecto> findAll(Pageable pageable);

    /**
     * Busca un proyecto por su ID, incluyendo todas sus tareas asociadas en una sola consulta optimizada.
     * @param id El ID del proyecto.
     * @return Un Optional que contiene el proyecto con sus tareas.
     */
    Optional<Proyecto> findByIdWithTasks(Long id);
}
