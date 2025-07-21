package com.example.gestionproyectos.servicio;

import com.example.gestionproyectos.dto.*;
import com.example.gestionproyectos.excepciones.RecursoNoEncontradoException;
import com.example.gestionproyectos.modelos.EstadoTarea;
import com.example.gestionproyectos.modelos.Proyecto;
import com.example.gestionproyectos.modelos.Tarea;
import com.example.gestionproyectos.repositorios.RepositorioProyecto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Usar la anotación de Spring

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImplementacionServicioProyecto implements ServicioProyecto {

    private final RepositorioProyecto repositorioProyecto;
    private final ModelMapper modelMapper; // Inyectar el bean de ModelMapper

    // Inyección de dependencias por constructor
    public ImplementacionServicioProyecto(RepositorioProyecto repositorioProyecto, ModelMapper modelMapper) {
        this.repositorioProyecto = repositorioProyecto;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ProyectoVerDTO crearProyecto(ProyectoCrearDTO proyectoCrearDTO) {
        // 1. Mapear el DTO de entrada a la entidad de dominio
        Proyecto proyecto = modelMapper.map(proyectoCrearDTO, Proyecto.class);

        // 2. Aplicar lógica de negocio (establecer valores por defecto)
        proyecto.setFechaCreacion(LocalDate.now());

        // 3. Persistir la entidad
        Proyecto proyectoGuardado = repositorioProyecto.save(proyecto);

        // 4. Mapear la entidad guardada al DTO de salida y devolverlo
        return modelMapper.map(proyectoGuardado, ProyectoVerDTO.class);
    }

    @Override
    @Transactional(readOnly = true) // Optimización para operaciones de solo lectura
    public List<ProyectoVerDTO> obtenerTodosLosProyectos() {
        List<Proyecto> proyectos = repositorioProyecto.findAll();
        // Mapear cada entidad en la lista a su DTO correspondiente
        return proyectos.stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoVerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProyectoVerDTO> obtenerProyectoPorId(Long id) {
        // Devolver un Optional del DTO, manteniendo el contrato
        return repositorioProyecto.findById(id)
                .map(proyecto -> modelMapper.map(proyecto, ProyectoVerDTO.class));
    }

    @Override
    @Transactional
    public ProyectoVerDTO actualizarProyecto(Long id, ProyectoActualizarDTO proyectoActualizarDTO) {
        // 1. Encontrar la entidad existente o lanzar una excepción clara
        Proyecto proyectoExistente = repositorioProyecto.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con ID: " + id));

        // 2. Actualizar solo los campos permitidos desde el DTO
        proyectoExistente.setNombre(proyectoActualizarDTO.getNombre());

        // 3. Persistir los cambios
        Proyecto proyectoActualizado = repositorioProyecto.save(proyectoExistente);

        // 4. Devolver el DTO con los datos actualizados
        return modelMapper.map(proyectoActualizado, ProyectoVerDTO.class);
    }

    @Override
    @Transactional
    public void eliminarProyecto(Long id) {
        // Verificar que el proyecto exista antes de intentar eliminarlo
        if (!repositorioProyecto.existsById(id)) {
            throw new RecursoNoEncontradoException("No se puede eliminar. Proyecto no encontrado con ID: " + id);
        }
        repositorioProyecto.deleteById(id);
    }

    @Override
    @Transactional
    public TareaVerDTO agregarTareaAProyecto(Long proyectoId, TareaCrearDTO tareaCrearDTO) {
        // 1. Encontrar el proyecto al que se agregará la tarea
        Proyecto proyecto = repositorioProyecto.findById(proyectoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con ID: " + proyectoId));

        // 2. Mapear el DTO de la tarea a la entidad Tarea
        Tarea nuevaTarea = modelMapper.map(tareaCrearDTO, Tarea.class);

        // 3. Aplicar lógica de negocio: establecer el estado inicial
        nuevaTarea.setEstado(EstadoTarea.PENDIENTE);

        // 4. Usar el método de ayuda para establecer la relación bidireccional
        proyecto.addTarea(nuevaTarea);

        // 5. Guardar el proyecto. Gracias a CascadeType.ALL, la nueva tarea se guardará también.
        repositorioProyecto.save(proyecto);

        // 6. Devolver el DTO de la tarea recién creada
        return modelMapper.map(nuevaTarea, TareaVerDTO.class);
    }
}