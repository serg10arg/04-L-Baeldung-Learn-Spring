package com.example.gestionproyectos.repositorios;

import com.example.gestionproyectos.modelos.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioProyecto  extends JpaRepository<Proyecto, Long> {

    /**
     * Encuentra un proyecto por su nombre.
     * Spring Data JPA generará automáticamente la implementación de esta consulta
     * basándose en el nombre del método.
     * @param nombre El nombre del proyecto a buscar.
     * @return Un Optional que contiene el proyecto si se encuentra, o vacío si no.
     */
    Optional<Proyecto> findByNombre(String nombre);
}
