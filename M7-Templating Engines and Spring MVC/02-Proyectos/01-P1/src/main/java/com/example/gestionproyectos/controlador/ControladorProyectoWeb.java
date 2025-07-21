package com.example.gestionproyectos.controlador;

import com.example.gestionproyectos.dto.ProyectoCrearDTO;
import com.example.gestionproyectos.dto.ProyectoVerDTO;
import com.example.gestionproyectos.servicio.ServicioProyecto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proyectos")
public class ControladorProyectoWeb {

    private static final Logger LOG = LoggerFactory.getLogger(ControladorProyectoWeb.class);

    private final ServicioProyecto servicioProyecto;

    // ANÁLISIS SENIOR: Se elimina la dependencia de ModelMapper.
    // El controlador no debe mapear objetos; esa es responsabilidad del servicio.
    @Autowired
    public ControladorProyectoWeb(ServicioProyecto servicioProyecto) {
        this.servicioProyecto = servicioProyecto;
    }

    /**
     * Muestra la lista de todos los proyectos.
     */
    @GetMapping
    public String listarProyectos(Model modelo) {
        LOG.info("Listando todos los proyectos...");
        // ANÁLISIS SENIOR: Se consume directamente la lista de DTOs del servicio.
        // El controlador no necesita saber nada sobre la entidad 'Proyecto'.
        List<ProyectoVerDTO> proyectosDTO = servicioProyecto.obtenerTodosLosProyectos();
        modelo.addAttribute("proyectos", proyectosDTO);
        return "proyectos/listaProyectos";
    }

    /**
     * Muestra el formulario para crear un nuevo proyecto.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model modelo) {
        LOG.info("Mostrando formulario para nuevo proyecto...");
        modelo.addAttribute("proyectoCrearDTO", new ProyectoCrearDTO());
        return "proyectos/crearProyecto";
    }

    /**
     * Procesa la solicitud para crear un nuevo proyecto.
     */
    @PostMapping("/nuevo")
    public String crearProyecto(@Valid @ModelAttribute("proyectoCrearDTO") ProyectoCrearDTO proyectoCrearDTO,
                                BindingResult resultadosValidacion,
                                RedirectAttributes atributosRedireccion) {
        LOG.info("Intentando crear nuevo proyecto: {}", proyectoCrearDTO.getNombre());

        if (resultadosValidacion.hasErrors()) {
            LOG.warn("Errores de validación al crear proyecto: {}", resultadosValidacion.getAllErrors());
            return "proyectos/crearProyecto";
        }

        // ANÁLISIS SENIOR: El servicio ya devuelve un DTO, pero para esta operación
        // de redirección, no necesitamos usar el valor de retorno.
        servicioProyecto.crearProyecto(proyectoCrearDTO);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Proyecto creado exitosamente!");
        LOG.info("Proyecto '{}' creado exitosamente.", proyectoCrearDTO.getNombre());
        return "redirect:/proyectos";
    }

    /**
     * Muestra los detalles de un proyecto específico.
     */
    @GetMapping("/{id}")
    public String verDetallesProyecto(@PathVariable Long id, Model modelo, RedirectAttributes atributosRedireccion) {
        LOG.info("Buscando detalles del proyecto con ID: {}", id);

        // ANÁLISIS SENIOR: Se consume directamente el Optional<ProyectoVerDTO> del servicio.
        // El código es más limpio, más seguro y respeta la arquitectura.
        Optional<ProyectoVerDTO> proyectoVerDTOOptional = servicioProyecto.obtenerProyectoPorId(id);

        if (proyectoVerDTOOptional.isPresent()) {
            modelo.addAttribute("proyecto", proyectoVerDTOOptional.get());
            return "proyectos/detallesProyecto";
        } else {
            LOG.warn("Proyecto con ID {} no encontrado.", id);
            atributosRedireccion.addFlashAttribute("mensajeError", "Proyecto no encontrado!");
            return "redirect:/proyectos";
        }
    }
}