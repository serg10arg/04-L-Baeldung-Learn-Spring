package com.example.integralproyecto.repositorio;

import com.example.integralproyecto.dto.proyecto.ProyectoResumenDTO;
import com.example.integralproyecto.modelo.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioProyecto extends JpaRepository<Proyecto, Long> {

    // --- Búsquedas Básicas (Existentes en tu código) ---

    /**
     * Busca un proyecto por su nombre exacto.
     * Genera: "SELECT p FROM Proyecto p WHERE p.nombre = ?1"
     */
    Optional<Proyecto> findByNombre(String nombre);

    /**
     * Busca proyectos creados después de una fecha específica.
     * Genera: "SELECT p FROM Proyecto p WHERE p.fechaCreacion > ?1"
     */
    List<Proyecto> findByFechaCreacionAfter(LocalDate fecha);

    /**
     * Busca proyectos cuyo nombre contenga una palabra clave, ignorando mayúsculas/minúsculas.
     * Genera: "SELECT p FROM Proyecto p WHERE upper(p.nombre) LIKE upper(concat('%', ?1, '%'))"
     */
    List<Proyecto> findByNombreContainingIgnoreCase(String keyword);


    // --- Búsquedas con Múltiples Criterios (AND / OR) ---

    /**
     * Busca proyectos que contengan una palabra clave en el nombre Y en la descripción.
     */
    List<Proyecto> findByNombreContainingIgnoreCaseAndDescripcionContainingIgnoreCase(String nombreKeyword, String descKeyword);

    /**
     * Busca proyectos que contengan una palabra clave en el nombre O en la descripción.
     */
    List<Proyecto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombreKeyword, String descKeyword);


    // --- Búsquedas por Rango (Between) ---

    /**
     * Busca proyectos creados dentro de un rango de fechas (inclusivo).
     * Genera: "SELECT p FROM Proyecto p WHERE p.fechaCreacion BETWEEN ?1 AND ?2"
     */
    List<Proyecto> findByFechaCreacionBetween(LocalDate fechaInicio, LocalDate fechaFin);


    // --- Ordenamiento y Limitación de Resultados ---

    /**
     * Busca los 5 proyectos más recientes, ordenados por fecha de creación descendente.
     */
    List<Proyecto> findTop5ByOrderByFechaCreacionDesc();

    /**
     * Busca todos los proyectos y los ordena por nombre de forma ascendente.
     */
    List<Proyecto> findAllByOrderByNombreAsc();


    // --- Consultas de Existencia y Conteo (Más eficientes que find...().isPresent()) ---

    /**
     * Verifica de forma eficiente si existe un proyecto con un nombre determinado.
     * Genera una consulta optimizada (usualmente SELECT COUNT(...) > 0).
     */
    boolean existsByNombre(String nombre);

    /**
     * Cuenta cuántos proyectos tienen una descripción.
     */
    long countByDescripcionIsNotNull();


    // --- Proyecciones (Práctica de Alto Rendimiento) ---

    /**
     * Devuelve una lista de DTOs de resumen en lugar de la entidad completa.
     * La consulta solo selecciona los campos necesarios (id, nombre), lo que es muy eficiente.
     * NOTA: Requiere un constructor en ProyectoResumenDTO que coincida con los campos: public ProyectoResumenDTO(Long id, String nombre)
     */
    @Query("SELECT new com.example.integralproyecto.dto.proyecto.ProyectoResumenDTO(p.id, p.nombre) FROM Proyecto p")
    List<ProyectoResumenDTO> findAllAsResumen();


    // --- Consulta Explícita con @Query (Existente en tu código) ---

    /**
     * Ejemplo de una consulta JPQL explícita para un control total.
     */
    @Query("SELECT p FROM Proyecto p WHERE p.nombre = :nombre AND p.descripcion IS NOT NULL")
    Optional<Proyecto> findByProyectoActivoPorNombre(@Param("nombre") String nombre);

}