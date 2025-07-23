package com.example.integralproyecto.servicio.impl;

import com.example.integralproyecto.dto.persona.PersonaActualizarDTO;
import com.example.integralproyecto.dto.persona.PersonaCrearDTO;
import com.example.integralproyecto.dto.persona.PersonaDetalleDTO;
import com.example.integralproyecto.dto.persona.PersonaResumenDTO;
import com.example.integralproyecto.excepcion.RecursoNoEncontradoException;
import com.example.integralproyecto.excepcion.RecursoYaExisteException; // Es una buena práctica crear esta excepción
import com.example.integralproyecto.modelo.Persona;
import com.example.integralproyecto.repositorio.RepositorioPersona;
import com.example.integralproyecto.servicio.ServicioPersona;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioPersonaImpl implements ServicioPersona {

    private final RepositorioPersona repositorioPersona;
    private final ModelMapper modelMapper;

    public ServicioPersonaImpl(RepositorioPersona repositorioPersona, ModelMapper modelMapper) {
        this.repositorioPersona = repositorioPersona;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaResumenDTO> listarTodasLasPersonas() {
        // Estrategia: Usar la proyección optimizada del repositorio. No se necesita mapeo.
        return repositorioPersona.findAllAsResumen();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaDetalleDTO obtenerPersonaPorId(Long id) {
        Persona persona = repositorioPersona.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona no encontrada con id: " + id));

        // Estrategia: Delegar la conversión al mapper centralizado.
        return modelMapper.map(persona, PersonaDetalleDTO.class);
    }

    @Override
    @Transactional
    public PersonaDetalleDTO crearPersona(PersonaCrearDTO personaCrearDTO) {
        // Estrategia: Encapsular la lógica de negocio (validación de unicidad).
        if (repositorioPersona.existsByCorreoElectronico(personaCrearDTO.getCorreoElectronico())) {
            throw new RecursoYaExisteException("Ya existe una persona con el correo electrónico: " + personaCrearDTO.getCorreoElectronico());
        }

        Persona persona = modelMapper.map(personaCrearDTO, Persona.class);
        Persona personaGuardada = repositorioPersona.save(persona);

        return modelMapper.map(personaGuardada, PersonaDetalleDTO.class);
    }

    @Override
    @Transactional
    public PersonaDetalleDTO actualizarPersona(Long id, PersonaActualizarDTO personaActualizarDTO) {
        Persona personaExistente = repositorioPersona.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona no encontrada con id: " + id));

        // Estrategia: Lógica de negocio avanzada para la actualización.
        // Si el correo se está cambiando, verificar que el nuevo no esté en uso por OTRO usuario.
        Optional<Persona> personaConNuevoEmail = repositorioPersona.findByCorreoElectronico(personaActualizarDTO.getCorreoElectronico());
        if (personaConNuevoEmail.isPresent() && !personaConNuevoEmail.get().getId().equals(id)) {
            throw new RecursoYaExisteException("El correo electrónico " + personaActualizarDTO.getCorreoElectronico() + " ya está en uso por otra persona.");
        }

        // ModelMapper actualiza los campos del objeto existente.
        modelMapper.map(personaActualizarDTO, personaExistente);
        Persona personaActualizada = repositorioPersona.save(personaExistente);

        return modelMapper.map(personaActualizada, PersonaDetalleDTO.class);
    }

    @Override
    @Transactional
    public void eliminarPersona(Long id) {
        // Estrategia: Usar el método más eficiente para la verificación de existencia.
        if (!repositorioPersona.existsById(id)) {
            throw new RecursoNoEncontradoException("No se puede eliminar. Persona no encontrada con id: " + id);
        }
        repositorioPersona.deleteById(id);
    }
}