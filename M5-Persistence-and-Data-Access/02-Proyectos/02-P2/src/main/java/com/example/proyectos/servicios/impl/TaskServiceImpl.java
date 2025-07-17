package com.example.proyectos.servicios.impl;

import com.example.proyectos.entidades.EstadoTarea;
import com.example.proyectos.entidades.Tarea;
import com.example.proyectos.repositorio.ITaskRepository;
import com.example.proyectos.servicios.ITaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // Declarar como un componente de servicio de Spring
public class TaskServiceImpl implements ITaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    // MEJORA: Usar inyección por constructor en lugar de @Autowired en el campo.
    // Esto hace las dependencias explícitas, obligatorias y finales, mejorando la robustez y la testeabilidad.
    private final ITaskRepository taskRepository;

    public TaskServiceImpl(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(readOnly = true) // Buena práctica para optimizar consultas de solo lectura.
    public Page<Tarea> findAll(Pageable pageable) {
        LOG.info("Buscando todas las tareas de forma paginada.");
        return taskRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tarea> findById(Long id) {
        LOG.info("Buscando tarea con ID: {}", id);
        return taskRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tarea> findByProyectoId(Long proyectoId) {
        LOG.info("Buscando todas las tareas para el proyecto con ID: {}", proyectoId);
        return taskRepository.findByProyectoId(proyectoId);
    }
}
