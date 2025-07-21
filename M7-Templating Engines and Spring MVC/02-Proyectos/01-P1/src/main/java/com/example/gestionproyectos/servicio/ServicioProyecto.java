package com.example.gestionproyectos.servicio;


import com.example.gestionproyectos.dto.*;
import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.Tarea;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para la gestión de proyectos.
 * Define las operaciones de negocio relacionadas con los proyectos.
 */
public interface ServicioProyecto {

    /**
     * Crea un nuevo proyecto a partir de un DTO de creación
     * @param proyectoCrearDTO el DTO que contiene los datos del nuevo proyecto
     * @return El DTO de visualización de proyecto recién creado
     */
    ProyectoVerDTO crearProyecto(ProyectoCrearDTO proyectoCrearDTO);

    /**
     * Obtiene todos los proyectos
     * @return Una lista de DTOs de visualización de todo los proyectos.
     */
    List<ProyectoVerDTO> obtenerTodosLosProyectos();

    /**
     * Obtiene un proyecto por su ID
     * @param id El ID del proyecto
     * @return Un Optional que contiene el DTO de visualización del proyecto si se encuentra, o vacío si no.
     */
    Optional<ProyectoVerDTO> obtenerProyectoPorId(Long id);

    /**
     * Actualiza un proyecto existente a partir de un DTO de actualización
     * @param id El ID del proyecto a actualizar
     * @param proyectoActualizarDTO El DTO que contiene los datos actualizados del proyecto
     * @return El DTO de visualización del proyecto actualizado
     */
    ProyectoVerDTO actualizarProyecto(Long id, ProyectoActualizarDTO proyectoActualizarDTO);

    /**
     * Elimina un proyecto por su ID
     * @param id el ID del proyecto a eliminar
     */
    void eliminarProyecto(Long id);

    /**
     * Agrega una nueva tarea a un proyecto específico
     * @param proyectoId El ID del proyecto al que se agregará la tarea
     * @param tareaCrearDTO El DTO que contiene los datos de la nueva tarea
     * @return El DTO de visualización de la tarea recién agregada
     */
    TareaVerDTO agregarTareaAProyecto(Long proyectoId, TareaCrearDTO tareaCrearDTO);
}