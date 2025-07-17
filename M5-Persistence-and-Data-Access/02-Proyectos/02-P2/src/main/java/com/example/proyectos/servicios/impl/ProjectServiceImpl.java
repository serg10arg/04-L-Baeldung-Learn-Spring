package com.example.proyectos.servicios.impl;

import com.example.proyectos.entidades.Proyecto;
import com.example.proyectos.repositorio.IProjectRepository;
import com.example.proyectos.servicios.IProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements IProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final IProjectRepository projectRepository;

    // Ya no necesitamos ITaskRepository aquí porque la persistencia se gestiona en cascada.
    public ProjectServiceImpl(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Implementación correcta y eficiente.
     * Construye el grafo de objetos en memoria y persiste el agregado raíz (Proyecto) una sola vez.
     * Gracias a CascadeType.ALL, JPA se encarga de guardar las Tareas asociadas.
     */
    @Override
    @Transactional
    public Proyecto crearProyecto(Proyecto proyecto) {
        LOG.info("Guardando un nuevo proyecto: {}", proyecto.getNombre());
        // La magia de CascadeType.ALL: una sola llamada guarda el proyecto y todas sus tareas.
        return projectRepository.save(proyecto);
    }

    @Override
    @Transactional(readOnly = true) // Buena práctica para operaciones de solo lectura
    public Page<Proyecto> findAll(Pageable pageable) {
        LOG.info("Buscando todos los proyectos de forma paginada.");
        return projectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proyecto> findByIdWithTasks(Long id) {
        LOG.info("Buscando proyecto con ID {} y cargando sus tareas.", id);
        return projectRepository.findByIdWithTareas(id);
    }
}
