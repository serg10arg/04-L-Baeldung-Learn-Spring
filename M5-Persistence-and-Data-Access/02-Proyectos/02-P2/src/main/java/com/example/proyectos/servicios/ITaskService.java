package com.example.proyectos.servicios;

import com.example.proyectos.entidades.EstadoTarea;
import com.example.proyectos.entidades.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITaskService {

    /**
     * Recupera todas las tareas de forma paginada.
     * @param pageable Objeto que contiene la información de paginación.
     * @return una `Page` de tareas.
     */
    Page<Tarea> findAll(Pageable pageable);

    /**
     * Encuentra una tarea por su ID.
     * @param id El ID de la tarea a buscar.
     * @return Un Optional que contiene la tarea si se encuentra.
     */
    Optional<Tarea> findById(Long id);

    /**
     * Busca todas las tareas asociadas a un proyecto específico.
     * @param proyectoId El ID del proyecto.
     * @return Una lista de tareas para ese proyecto.
     */
    List<Tarea> findByProyectoId(Long proyectoId);
}
