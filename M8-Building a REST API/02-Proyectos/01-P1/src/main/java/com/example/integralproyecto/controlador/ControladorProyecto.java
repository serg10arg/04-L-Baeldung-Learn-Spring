package com.example.integralproyecto.controlador;

// 1. Importar los DTOs especializados
import com.example.integralproyecto.dto.proyecto.ProyectoActualizarDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoCrearDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoDetalleDTO;
import com.example.integralproyecto.dto.proyecto.ProyectoResumenDTO;
import com.example.integralproyecto.servicio.ServicioProyecto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST para la gestión de proyectos.
 * Expone endpoints para operaciones de negocio, comunicándose exclusivamente
 * a través de DTOs y delegando toda la lógica a la capa de servicio.
 */
@RestController
@RequestMapping("/api/proyectos")
public class ControladorProyecto {

    private final ServicioProyecto servicioProyecto;

    // La inyección por constructor es la mejor práctica. @Autowired es opcional aquí.
    public ControladorProyecto(ServicioProyecto servicioProyecto) {
        this.servicioProyecto = servicioProyecto;
    }

    /**
     * GET /api/proyectos : Obtiene una lista resumida de todos los proyectos.
     * @return ResponseEntity con una lista de ProyectoResumenDTO y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<ProyectoResumenDTO>> listarTodosLosProyectos() {
        // 2. El controlador solo llama al servicio. No hay lógica de mapeo.
        List<ProyectoResumenDTO> proyectos = servicioProyecto.listarTodosLosProyectos();
        return ResponseEntity.ok(proyectos);
    }

    /**
     * GET /api/proyectos/{id} : Obtiene los detalles completos de un proyecto.
     * @param id El ID del proyecto.
     * @return ResponseEntity con el ProyectoDetalleDTO y estado 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDetalleDTO> obtenerProyectoPorId(@PathVariable Long id) {
        // 3. La lógica de "no encontrado" está en el servicio. El controlador solo llama.
        ProyectoDetalleDTO proyecto = servicioProyecto.obtenerProyectoPorId(id);
        return ResponseEntity.ok(proyecto);
    }

    /**
     * POST /api/proyectos : Crea un nuevo proyecto.
     * @param proyectoCrearDTO El DTO con los datos para la creación.
     * @return ResponseEntity con el ProyectoDetalleDTO creado, la URI del nuevo recurso y estado 201 Created.
     */
    @PostMapping
    public ResponseEntity<ProyectoDetalleDTO> crearProyecto(@Valid @RequestBody ProyectoCrearDTO proyectoCrearDTO) {
        ProyectoDetalleDTO proyectoCreado = servicioProyecto.crearProyecto(proyectoCrearDTO);

        // Construye la URI del nuevo recurso creado para el encabezado 'Location'.
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(proyectoCreado.getId()).toUri();

        return ResponseEntity.created(ubicacion).body(proyectoCreado);
    }

    /**
     * PUT /api/proyectos/{id} : Actualiza un proyecto existente.
     * @param id El ID del proyecto a actualizar.
     * @param proyectoActualizarDTO El DTO con los datos actualizados.
     * @return ResponseEntity con el ProyectoDetalleDTO actualizado y estado 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDetalleDTO> actualizarProyecto(@PathVariable Long id, @Valid @RequestBody ProyectoActualizarDTO proyectoActualizarDTO) {
        ProyectoDetalleDTO proyectoActualizado = servicioProyecto.actualizarProyecto(id, proyectoActualizarDTO);
        return ResponseEntity.ok(proyectoActualizado);
    }

    /**
     * DELETE /api/proyectos/{id} : Elimina un proyecto por su ID.
     * @param id El ID del proyecto a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content si tiene éxito.
    public void eliminarProyecto(@PathVariable Long id) {
        // 4. La excepción de "no encontrado" se maneja en el servicio y se traduce en el ManejadorExcepcionesGlobal.
        servicioProyecto.eliminarProyecto(id);
    }

    // 5. Los métodos de conversión han sido eliminados. ¡Ya no son responsabilidad del controlador!
}