package com.example.integralproyecto.servicio;

import com.example.integralproyecto.dto.proyecto.ProyectoActualizarDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoCrearDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoDetalleDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoResumenDTO;

import java.util.List;

/**
 * Define el contrato para las operaciones de negocio relacionadas con Proyectos.
 * Esta capa se comunica utilizando DTOs y encapsula toda la lógica de negocio.
 */
public interface ServicioProyecto {

    /**
     * Obtiene una lista resumida de todos los proyectos.
     * Ideal para vistas de lista, optimizando el rendimiento.
     * @return Una lista de ProyectoResumenDTO.
     */
    List<ProyectoResumenDTO> listarTodosLosProyectos();

    /**
     * Busca un proyecto por su ID y devuelve una vista detallada.
     * @param id El ID del proyecto a buscar.
     * @return Un ProyectoDetalleDTO con la información completa.
     * @throws com.example.integralproyecto.excepciones.RecursoNoEncontradoException si el proyecto no existe.
     */
    ProyectoDetalleDTO obtenerProyectoPorId(Long id);

    /**
     * Crea un nuevo proyecto a partir de los datos proporcionados.
     * Valida que no exista otro proyecto con el mismo nombre.
     * @param proyectoCrearDTO DTO con los datos para la creación.
     * @return Un ProyectoDetalleDTO del proyecto recién creado.
     * @throws com.example.integralproyecto.excepciones.RecursoYaExisteException si ya existe un proyecto con ese nombre.
     */
    ProyectoDetalleDTO crearProyecto(ProyectoCrearDTO proyectoCrearDTO);

    /**
     * Actualiza un proyecto existente.
     * @param id El ID del proyecto a actualizar.
     * @param proyectoActualizarDTO DTO con los nuevos datos.
     * @return Un ProyectoDetalleDTO del proyecto actualizado.
     * @throws com.example.integralproyecto.excepciones.RecursoNoEncontradoException si el proyecto no existe.
     */
    ProyectoDetalleDTO actualizarProyecto(Long id, ProyectoActualizarDTO proyectoActualizarDTO);

    /**
     * Elimina un proyecto por su ID.
     * @param id El ID del proyecto a eliminar.
     * @throws com.example.integralproyecto.excepciones.RecursoNoEncontradoException si el proyecto no existe.
     */
    void eliminarProyecto(Long id);

}