package com.example.proyectos.repositorio;

import com.example.proyectos.entidades.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IProjectRepository extends JpaRepository<Proyecto, Long> {

    /**
     * Busca un proyecto por su ID y carga ansiosamente su colección de tareas
     * en la misma consulta para evitar el problema de N+1 consultas.
     *
     * @param id El ID del proyecto a buscar.
     * @return Un Optional que contiene el Proyecto con sus tareas inicializadas.
     */
    @Query("SELECT p FROM Proyecto p LEFT JOIN FETCH p.tareas WHERE p.id = :id")
    Optional<Proyecto> findByIdWithTareas(@Param("id") Long id);
}
