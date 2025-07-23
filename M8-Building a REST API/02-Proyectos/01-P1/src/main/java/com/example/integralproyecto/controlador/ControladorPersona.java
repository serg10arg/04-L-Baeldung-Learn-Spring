package com.example.integralproyecto.controlador;

// 1. Importar los DTOs especializados y las excepciones correctas
import com.example.integralproyecto.dto.persona.PersonaActualizarDTO;
import com.example.integralproyecto.dto.persona.PersonaCrearDTO;
import com.example.integralproyecto.dto.persona.PersonaDetalleDTO;
import com.example.integralproyecto.dto.persona.PersonaResumenDTO;
import com.example.integralproyecto.servicio.ServicioPersona;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST para la gestión de personas.
 * Expone endpoints para operaciones de negocio, comunicándose exclusivamente
 * a través de DTOs y delegando toda la lógica a la capa de servicio.
 */
@RestController
@RequestMapping("/api/personas")
public class ControladorPersona {

    private final ServicioPersona servicioPersona;

    // 2. Inyección por constructor es la mejor práctica. @Autowired es redundante.
    public ControladorPersona(ServicioPersona servicioPersona) {
        this.servicioPersona = servicioPersona;
    }

    /**
     * GET /api/personas : Obtiene una lista resumida de todas las personas.
     * @return ResponseEntity con una lista de PersonaResumenDTO y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<PersonaResumenDTO>> listarTodasLasPersonas() {
        // 3. El controlador solo llama al servicio. No hay lógica de mapeo ni streams.
        List<PersonaResumenDTO> personas = servicioPersona.listarTodasLasPersonas();
        return ResponseEntity.ok(personas);
    }

    /**
     * GET /api/personas/{id} : Obtiene los detalles completos de una persona.
     * @param id El ID de la persona.
     * @return ResponseEntity con el PersonaDetalleDTO y estado 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDetalleDTO> obtenerPersonaPorId(@PathVariable Long id) {
        // 4. La lógica de "no encontrado" está en el servicio. El controlador solo llama.
        PersonaDetalleDTO persona = servicioPersona.obtenerPersonaPorId(id);
        return ResponseEntity.ok(persona);
    }

    /**
     * POST /api/personas : Crea una nueva persona.
     * @param personaCrearDTO El DTO con los datos para la creación.
     * @return ResponseEntity con el PersonaDetalleDTO creado, la URI del nuevo recurso y estado 201 Created.
     */
    @PostMapping
    public ResponseEntity<PersonaDetalleDTO> crearPersona(@Valid @RequestBody PersonaCrearDTO personaCrearDTO) {
        PersonaDetalleDTO personaCreada = servicioPersona.crearPersona(personaCrearDTO);

        // Construye la URI del nuevo recurso creado para el encabezado 'Location'.
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(personaCreada.getId()).toUri();

        return ResponseEntity.created(ubicacion).body(personaCreada);
    }

    /**
     * PUT /api/personas/{id} : Actualiza una persona existente.
     * @param id El ID de la persona a actualizar.
     * @param personaActualizarDTO El DTO con los datos actualizados.
     * @return ResponseEntity con el PersonaDetalleDTO actualizado y estado 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonaDetalleDTO> actualizarPersona(@PathVariable Long id, @Valid @RequestBody PersonaActualizarDTO personaActualizarDTO) {
        PersonaDetalleDTO personaActualizada = servicioPersona.actualizarPersona(id, personaActualizarDTO);
        return ResponseEntity.ok(personaActualizada);
    }

    /**
     * DELETE /api/personas/{id} : Elimina una persona por su ID.
     * @param id El ID de la persona a eliminar.
     * @return ResponseEntity con estado 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPersona(@PathVariable Long id) {
        // 5. La excepción de "no encontrado" se maneja en el servicio y se traduce en el ManejadorExcepcionesGlobal.
        servicioPersona.eliminarPersona(id);
    }

    // 6. Los métodos de conversión han sido eliminados. ¡Ya no son responsabilidad del controlador!
}