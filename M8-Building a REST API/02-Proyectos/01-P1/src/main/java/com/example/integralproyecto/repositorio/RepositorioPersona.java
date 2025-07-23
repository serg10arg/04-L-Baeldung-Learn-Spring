package com.example.integralproyecto.repositorio;

import com.example.integralproyecto.dto.persona.PersonaResumenDTO;
import com.example.integralproyecto.modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioPersona extends JpaRepository<Persona, Long> {

    // --- Búsquedas Básicas por Atributos Únicos ---

    /**
     * Busca una persona por su correo electrónico exacto. Es la forma más eficiente
     * de encontrar un usuario único por un campo que no es el ID.
     * Genera: "SELECT p FROM Persona p WHERE p.correoElectronico = ?1"
     */
    Optional<Persona> findByCorreoElectronico(String correoElectronico);


    // --- Búsquedas con Múltiples Criterios (AND / OR) ---

    /**
     * Busca personas que contengan una palabra clave en el nombre Y cuya edad sea menor a un límite.
     * Genera: "SELECT p FROM Persona p WHERE p.nombre LIKE %?1% AND p.edad < ?2"
     */
    List<Persona> findByNombreContainingAndEdadLessThan(String keyword, int edadMaxima);

    /**
     * Busca personas cuya edad sea mayor o igual a un valor O cuyo nombre comience con un prefijo.
     */
    List<Persona> findByEdadGreaterThanEqualOrNombreStartingWith(int edad, String prefijo);


    // --- Búsquedas por Rango (Between) ---

    /**
     * Busca personas cuya edad se encuentre dentro de un rango específico (inclusivo).
     * Genera: "SELECT p FROM Persona p WHERE p.edad BETWEEN ?1 AND ?2"
     */
    List<Persona> findByEdadBetween(int edadMinima, int edadMaxima);


    // --- Ordenamiento y Limitación de Resultados ---

    /**
     * Busca las 3 personas de mayor edad en el sistema.
     */
    List<Persona> findTop3ByOrderByEdadDesc();

    /**
     * Busca todas las personas y las ordena alfabéticamente por nombre.
     */
    List<Persona> findAllByOrderByNombreAsc();


    // --- Consultas de Existencia y Conteo (Optimizadas) ---

    /**
     * Verifica de forma muy eficiente si ya existe una persona con un correo electrónico.
     * Es preferible a findByCorreoElectronico(...).isPresent() porque no trae el objeto.
     */
    boolean existsByCorreoElectronico(String correoElectronico);

    /**
     * Cuenta cuántas personas son mayores de edad.
     */
    long countByEdadGreaterThanEqual(int edad);


    // --- Proyecciones (Práctica de Alto Rendimiento) ---

    /**
     * Devuelve una lista de DTOs de resumen en lugar de la entidad completa.
     * La consulta solo selecciona los campos necesarios (id, nombre, correo), lo que es muy eficiente.
     * NOTA: Requiere un constructor en PersonaResumenDTO que coincida con los campos.
     */
    @Query("SELECT new com.example.integralproyecto.dto.persona.PersonaResumenDTO(p.id, p.nombre, p.correoElectronico) FROM Persona p")
    List<PersonaResumenDTO> findAllAsResumen();


    // --- Consulta Explícita con @Query (Existente en tu código) ---

    /**
     * Ejemplo de una consulta JPQL explícita para buscar por el dominio del correo.
     */
    @Query("SELECT p FROM Persona p WHERE p.correoElectronico LIKE %:dominio")
    List<Persona> findByDominioCorreo(@Param("dominio") String dominio);

}