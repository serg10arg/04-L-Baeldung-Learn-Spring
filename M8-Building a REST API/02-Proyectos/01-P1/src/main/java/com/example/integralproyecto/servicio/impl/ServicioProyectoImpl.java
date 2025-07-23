package com.example.integralproyecto.servicio.impl;

import com.example.integralproyecto.dto.proyecto.ProyectoActualizarDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoCrearDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoDetalleDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoResumenDTO;
import com.example.integralproyecto.modelo.Proyecto;
import com.example.integralproyecto.repositorio.RepositorioProyecto;
import com.example.integralproyecto.servicio.ServicioProyecto;
// Importarías tus excepciones personalizadas aquí
// import com.example.integralproyecto.excepciones.RecursoNoEncontradoException;
// import com.example.integralproyecto.excepciones.RecursoYaExisteException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioProyectoImpl implements ServicioProyecto {

    private final RepositorioProyecto repositorioProyecto;
    private final ModelMapper modelMapper; // Inyectamos el Mapper

    // El constructor ahora recibe ambas dependencias. Spring se encarga de proveerlas.
    public ServicioProyectoImpl(RepositorioProyecto repositorioProyecto, ModelMapper modelMapper) {
        this.repositorioProyecto = repositorioProyecto;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResumenDTO> listarTodosLosProyectos() {
        // Esta consulta ya devuelve DTOs, por lo que no necesita mapeo aquí. ¡Perfecto!
        return repositorioProyecto.findAllAsResumen();
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDetalleDTO obtenerProyectoPorId(Long id) {
        Proyecto proyecto = repositorioProyecto.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id)); // Reemplazar con RecursoNoEncontradoException

        // Usamos el mapper para convertir la entidad a DTO
        return modelMapper.map(proyecto, ProyectoDetalleDTO.class);
    }

    @Override
    @Transactional
    public ProyectoDetalleDTO crearProyecto(ProyectoCrearDTO proyectoCrearDTO) {
        if (repositorioProyecto.existsByNombre(proyectoCrearDTO.getNombre())) {
            throw new RuntimeException("Ya existe un proyecto con el nombre: " + proyectoCrearDTO.getNombre()); // Reemplazar con RecursoYaExisteException
        }

        // Usamos el mapper para convertir el DTO de creación a una entidad
        Proyecto proyecto = modelMapper.map(proyectoCrearDTO, Proyecto.class);

        // La lógica de negocio sigue perteneciendo al servicio
        proyecto.setFechaCreacion(LocalDate.now());

        Proyecto proyectoGuardado = repositorioProyecto.save(proyecto);

        // Mapeamos la entidad guardada (con ID y fecha) al DTO de detalle para la respuesta
        return modelMapper.map(proyectoGuardado, ProyectoDetalleDTO.class);
    }

    @Override
    @Transactional
    public ProyectoDetalleDTO actualizarProyecto(Long id, ProyectoActualizarDTO proyectoActualizarDTO) {
        Proyecto proyectoExistente = repositorioProyecto.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id)); // Reemplazar con RecursoNoEncontradoException

        // ModelMapper es lo suficientemente inteligente para actualizar un objeto existente.
        // Copiará los campos coincidentes de proyectoActualizarDTO a proyectoExistente.
        modelMapper.map(proyectoActualizarDTO, proyectoExistente);

        Proyecto proyectoActualizado = repositorioProyecto.save(proyectoExistente);

        return modelMapper.map(proyectoActualizado, ProyectoDetalleDTO.class);
    }

    @Override
    @Transactional
    public void eliminarProyecto(Long id) {
        if (!repositorioProyecto.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con id: " + id); // Reemplazar con RecursoNoEncontradoException
        }
        repositorioProyecto.deleteById(id);
    }

    // --- Los métodos de mapeo privados han sido eliminados ---
    // La responsabilidad ahora recae en el bean de ModelMapper.
}