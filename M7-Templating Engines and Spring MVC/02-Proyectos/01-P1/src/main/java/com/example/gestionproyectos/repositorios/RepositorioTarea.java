package com.example.gestionproyectos.repositorios;

import com.example.gestionproyectos.modelos.EstadoTarea;
import com.example.gestionproyectos.modelos.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioTarea extends JpaRepository<Tarea, Long> {
    // Spring Data JPA proveerá las implementaciones CRUD básicas automáticamente.
    // Se pueden añadir métodos de consulta derivados aquí si son necesarios.

    /**
     * Encuentra todas las tareas asociadas a un proyecto específico.
     * Útil para obtener las tareas de un proyecto sin cargar toda la entidad Proyecto.
     * @param proyectoId El ID del proyecto.
     * @return Una lista de tareas para el proyecto dado.
     */
    List<Tarea> findByProyectoId(Long proyectoId);

    /**
     * Encuentra todas las tareas que se encuentran en un estado particular.
     * Útil para generar informes o tableros (e.g., mostrar todas las tareas pendientes).
     * @param estado El estado de la tarea a buscar.
     * @return Una lista de tareas que coinciden con el estado.
     */
    List<Tarea> findByEstado(EstadoTarea estado);
}
