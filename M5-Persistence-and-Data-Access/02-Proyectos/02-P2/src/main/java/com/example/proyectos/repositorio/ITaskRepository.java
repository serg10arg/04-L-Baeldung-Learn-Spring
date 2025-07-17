package com.example.proyectos.repositorio;

import com.example.proyectos.entidades.EstadoTarea;
import com.example.proyectos.entidades.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ITaskRepository  extends JpaRepository<Tarea, Long> {

    /**
     * Encuentra todas las tareas asociadas a un ID de proyecto específico.
     * Spring Data JPA genera la consulta a partir del nombre del método.
     * @param proyectoId El ID del proyecto.
     * @return Una lista de tareas para ese proyecto.
     */
    List<Tarea> findByProyectoId(Long proyectoId);

    /**
     * Encuentra todas las tareas que se encuentran en un estado particular.
     * @param estado El estado de la tarea a buscar.
     * @return Una lista de tareas que coinciden con el estado.
     */
    List<Tarea> findByEstado(EstadoTarea estado);

    List<Tarea> findByFechaVencimientoBefore(LocalDate fecha);
}
