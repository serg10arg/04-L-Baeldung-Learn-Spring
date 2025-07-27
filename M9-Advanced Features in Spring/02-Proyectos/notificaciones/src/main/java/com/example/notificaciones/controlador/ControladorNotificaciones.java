package com.example.notificaciones.controlador;

import com.example.notificaciones.dto.EventoNotificacionDTO;
import com.example.notificaciones.modelo.Notificacion;
import com.example.notificaciones.servicio.ServicioNotificaciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; //
import org.springframework.security.core.Authentication; //
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux; //
import reactor.core.publisher.Mono; //

/**
 * Controlador RESTful para gestionar la recepción de eventos y la consulta de historial de notificaciones.
 * Utiliza Spring WebFlux para manejar peticiones HTTP reactivas y no bloqueantes.
 */
@RestController // Marca la clase como un controlador REST [concepto_Spring]
@RequestMapping("/api/notificaciones") // Define el prefijo de la ruta para todos los endpoints en esta clase [concepto_Spring]
public class ControladorNotificaciones {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ControladorNotificaciones.class);

    private final ServicioNotificaciones servicioNotificaciones;

    /**
     * Constructor para inyectar el servicio de notificaciones.
     *
     * @param servicioNotificaciones El servicio que maneja la lógica de negocio de las notificaciones.
     */
    public ControladorNotificaciones(ServicioNotificaciones servicioNotificaciones) {
        this.servicioNotificaciones = servicioNotificaciones;
    }

    /**
     * Endpoint para recibir eventos de otros sistemas.
     * Este es el "punto de entrada" para nuevas notificaciones.
     *
     * @param evento El cuerpo de la petición que contiene los datos del evento.
     * @return Un Mono de ResponseEntity con la notificación guardada y un estado HTTP 201 (Created).
     */
    @PostMapping("/eventos") // Mapea peticiones POST a /api/notificaciones/eventos
    @ResponseStatus(HttpStatus.CREATED) // Retorna un estado HTTP 201 si la operación es exitosa
    public Mono<Notificacion> recibirEventoNotificacion(@RequestBody EventoNotificacionDTO evento) {
        REGISTRO.info("Recibiendo nuevo evento: {}", evento.getTipo());
        return servicioNotificaciones.procesarYEnviarNotificacion(evento); // Delega al servicio para procesar y guardar
    }

    /**
     * Endpoint para consultar el historial de notificaciones de un usuario.
     * La autorización asegura que un usuario solo pueda ver sus propias notificaciones.
     *
     * @param authentication Objeto Authentication inyectado por Spring Security, representa al usuario autenticado.
     * @param leida Parámetro opcional para filtrar por estado de lectura.
     * @return Un Flux de notificaciones para el usuario autenticado.
     */
    @GetMapping("/historial") // Mapea peticiones GET a /api/notificaciones/historial
    // La expresión SpEL en @PreAuthorize asegura que solo usuarios autenticados puedan acceder.
    // El servicio se encarga de filtrar por el usuario autenticado.
    @PreAuthorize("isAuthenticated()") // Solo usuarios autenticados pueden acceder
    public Flux<Notificacion> obtenerHistorialNotificaciones(
            Authentication authentication, // Inyección del objeto de autenticación
            @RequestParam(required = false) Boolean leida) { //
        String identificadorUsuario = authentication.getName(); // El nombre de usuario autenticado es el identificador [contexto_seguridad]
        REGISTRO.info("Consulta de historial para usuario: {}", identificadorUsuario);
        return servicioNotificaciones.obtenerHistorialNotificaciones(identificadorUsuario, leida); //
    }

    /**
     * Endpoint para que un administrador consulte todas las notificaciones.
     * Requiere el rol 'ADMIN' para acceder.
     *
     * @param authentication Objeto Authentication.
     * @param leida Parámetro opcional para filtrar por estado de lectura.
     * @return Un Flux de todas las notificaciones.
     */
    @GetMapping("/historial/todas")
    @PreAuthorize("hasRole('ADMIN')") // Solo usuarios con rol ADMIN pueden acceder
    public Flux<Notificacion> obtenerTodasLasNotificaciones(
            Authentication authentication,
            @RequestParam(required = false) Boolean leida) {
        REGISTRO.info("Consulta de todas las notificaciones por administrador: {}", authentication.getName());
        return servicioNotificaciones.obtenerTodasLasNotificaciones(leida); //
    }

    /**
     * Endpoint para marcar una notificación como leída.
     * La autorización asegura que un usuario solo pueda marcar como leída su propia notificación.
     *
     * @param id El ID de la notificación.
     * @param authentication Objeto Authentication.
     * @return Un Mono de la notificación actualizada, o Mono.empty() si no se encuentra o no pertenece al usuario.
     */
    @PutMapping("/{id}/leida")
    @PreAuthorize("isAuthenticated()") // Solo usuarios autenticados pueden acceder
    public Mono<ResponseEntity<Notificacion>> marcarNotificacionComoLeida(
            @PathVariable String id,
            Authentication authentication) {
        String identificadorUsuario = authentication.getName();
        REGISTRO.info("Marcando notificación {} como leída para usuario {}", id, identificadorUsuario);
        return servicioNotificaciones.marcarComoLeida(id, identificadorUsuario)
                .map(ResponseEntity::ok) // Si se encuentra y actualiza, devuelve 200 OK con la notificación
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra o no pertenece al usuario, devuelve 404 Not Found
    }
}
