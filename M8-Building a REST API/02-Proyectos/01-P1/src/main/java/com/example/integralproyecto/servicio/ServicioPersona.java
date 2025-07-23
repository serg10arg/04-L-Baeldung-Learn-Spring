package com.example.integralproyecto.servicio;

import com.example.integralproyecto.dto.persona.PersonaActualizarDTO;
import com.example.integralproyecto.dto.persona.PersonaCrearDTO;
import com.example.integralproyecto.dto.persona.PersonaDetalleDTO;
import com.example.integralproyecto.dto.persona.PersonaResumenDTO;
import com.example.integralproyecto.excepcion.RecursoNoEncontradoException;
// Asumimos que existe una excepción similar para recursos duplicados
// import com.example.integralproyecto.excepcion.RecursoYaExisteException;

import java.util.List;

/**
 * Define el contrato para las operaciones de negocio relacionadas con Personas.
 * Esta capa se comunica utilizando DTOs y encapsula toda la lógica de negocio,
 * como la validación de unicidad de correos electrónicos.
 */
public interface ServicioPersona {

    /**
     * Obtiene una lista resumida de todas las personas.
     * Utiliza una proyección optimizada para mejorar el rendimiento.
     * @return Una lista de PersonaResumenDTO.
     */
    List<PersonaResumenDTO> listarTodasLasPersonas();

    /**
     * Busca una persona por su ID y devuelve una vista detallada.
     * @param id El ID de la persona a buscar.
     * @return Un PersonaDetalleDTO con la información completa.
     * @throws RecursoNoEncontradoException si la persona no existe.
     */
    PersonaDetalleDTO obtenerPersonaPorId(Long id);

    /**
     * Crea una nueva persona a partir de los datos proporcionados.
     * Valida que el correo electrónico no esté ya en uso.
     * @param personaCrearDTO DTO con los datos para la creación.
     * @return Un PersonaDetalleDTO de la persona recién creada.
     * @throws com.example.integralproyecto.excepcion.RecursoYaExisteException si ya existe una persona con ese correo.
     */
    PersonaDetalleDTO crearPersona(PersonaCrearDTO personaCrearDTO);

    /**
     * Actualiza una persona existente.
     * @param id El ID de la persona a actualizar.
     * @param personaActualizarDTO DTO con los nuevos datos.
     * @return Un PersonaDetalleDTO de la persona actualizada.
     * @throws RecursoNoEncontradoException si la persona no existe.
     */
    PersonaDetalleDTO actualizarPersona(Long id, PersonaActualizarDTO personaActualizarDTO);

    /**
     * Elimina una persona por su ID.
     * @param id El ID de la persona a eliminar.
     * @throws RecursoNoEncontradoException si la persona no existe.
     */
    void eliminarPersona(Long id);

}