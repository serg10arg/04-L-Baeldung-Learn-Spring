package com.example.notificaciones.servicio;

import com.example.notificaciones.dto.EventoNotificacionDTO;
import com.example.notificaciones.modelo.Notificacion;
import com.example.notificaciones.repositorio.RepositorioNotificacionesReactivo;
import com.example.notificaciones.manejador.ManejadorWebSocketNotificaciones; // Necesario para notificaciones en tiempo real
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener; //
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux; //
import reactor.core.publisher.Mono; //

/**
 * Servicio central para la gestión de notificaciones.
 * Encapsula la lógica de negocio: recepción, almacenamiento y envío de notificaciones.
 * Demuestra el uso de programación reactiva con Mono/Flux y arquitectura orientada a eventos.
 */
@Service // Marca la clase como un componente de servicio [concepto_Spring]
public class ServicioNotificaciones {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioNotificaciones.class);

    private final RepositorioNotificacionesReactivo repositorioNotificaciones;
    private final ManejadorWebSocketNotificaciones manejadorWebSocket;

    /**
     * Constructor para inyectar el repositorio de notificaciones y el manejador de WebSocket.
     *
     * @param repositorioNotificaciones El repositorio para interactuar con MongoDB.
     * @param manejadorWebSocket El manejador para enviar notificaciones vía WebSocket.
     */
    public ServicioNotificaciones(RepositorioNotificacionesReactivo repositorioNotificaciones,
                                  ManejadorWebSocketNotificaciones manejadorWebSocket) {
        this.repositorioNotificaciones = repositorioNotificaciones;
        this.manejadorWebSocket = manejadorWebSocket;
    }

// Dentro de la clase ServicioNotificaciones

    /**
     * Procesa un evento de notificación recibido, lo guarda y lo envía en tiempo real.
     * Este método puede ser invocado por un endpoint HTTP o por un Spring Event.
     *
     * @param evento El DTO del evento de notificación a procesar.
     * @return Un Mono que representa la operación de guardado y envío de la notificación.
     */
    public Mono<Notificacion> procesarYEnviarNotificacion(EventoNotificacionDTO evento) {
        REGISTRO.info("Procesando evento de notificación para el usuario: {}", evento.getIdentificadorUsuarioDestino());
        Notificacion nuevaNotificacion = new Notificacion(
                evento.getIdentificadorUsuarioDestino(),
                evento.getTipo(),
                evento.getMensaje()
        );

        // Almacenar la notificación en MongoDB
        return repositorioNotificaciones.save(nuevaNotificacion)
                .doOnSuccess(notificacionGuardada -> REGISTRO.info("Notificación guardada en DB con ID: {}", notificacionGuardada.getId()))
                .flatMap(notificacionGuardada ->
                        // El envío por WebSocket ahora es parte de la cadena principal.
                        // flatMap espera un Publisher, y nuestro manejador devuelve un Mono<Void>.
                        // El .then() asegura que después de que el envío se complete, la cadena continúe
                        // y devuelva la notificacionGuardada original.
                        manejadorWebSocket.enviarNotificacionAUsuario(
                                        notificacionGuardada.getIdentificadorUsuarioDestino(),
                                        notificacionGuardada
                                )
                                .doOnSuccess(v -> REGISTRO.info("Notificación enviada por WebSocket a usuario: {}", notificacionGuardada.getIdentificadorUsuarioDestino()))
                                .doOnError(error -> REGISTRO.error("Error al enviar notificación por WebSocket: {}", error.getMessage()))
                                .then(Mono.just(notificacionGuardada)) // Devuelve la notificación después de que el envío se complete.
                )
                .doOnError(error -> REGISTRO.error("Error en el flujo de procesamiento de notificación: {}", error.getMessage()));
    }

    /**
     * Escucha eventos internos de Spring (simulados) y los procesa.
     * Demuestra la comunicación interna basada en eventos para desacoplamiento.
     *
     * @param eventoInterno El evento interno de notificación.
     */
    @EventListener //
    public void manejarEventoInterno(EventoNotificacionDTO eventoInterno) {
        REGISTRO.info("Evento interno de Spring recibido: {}", eventoInterno.getTipo());
        procesarYEnviarNotificacion(eventoInterno).subscribe(); // Se suscribe para activar el flujo reactivo
    }

    /**
     * Consulta el historial de notificaciones para un usuario específico.
     * Implementa lógica de filtrado y paginación si es necesario (aquí se muestra un ejemplo simple de filtrado).
     *
     * @param identificadorUsuario El ID del usuario.
     * @param leida Filtro opcional para notificaciones leídas/no leídas. Si es nulo, devuelve todas.
     * @return Un Flux de notificacion para el usuario.
     */
    public Flux<Notificacion> obtenerHistorialNotificaciones(String identificadorUsuario, Boolean leida) {
        REGISTRO.debug("Obteniendo historial para usuario: {} con estado de lectura: {}", identificadorUsuario, leida);
        if (leida != null) {
            return repositorioNotificaciones.findByIdentificadorUsuarioDestinoAndLeida(identificadorUsuario, leida); //
        }
        return repositorioNotificaciones.findByIdentificadorUsuarioDestino(identificadorUsuario); //
    }

    /**
     * Marca una notificación como leída.
     *
     * @param idNotificacion El ID de la notificación a marcar.
     * @param identificadorUsuario El ID del usuario propietario de la notificación.
     * @return Un Mono de la notificación actualizada, o Mono.empty() si no se encuentra o no pertenece al usuario.
     */
    public Mono<Notificacion> marcarComoLeida(String idNotificacion, String identificadorUsuario) {
        return repositorioNotificaciones.findById(idNotificacion) //
                .filter(notificacion -> notificacion.getIdentificadorUsuarioDestino().equals(identificadorUsuario)) // Asegura que solo el propietario pueda marcar como leída
                .flatMap(notificacion -> {
                    if (!notificacion.isLeida()) {
                        notificacion.setLeida(true);
                        return repositorioNotificaciones.save(notificacion); //
                    }
                    return Mono.just(notificacion); // Ya estaba leída, devuelve la misma notificación
                });
    }

    /**
     * Obtiene todas las notificaciones (para administradores).
     *
     * @param leida Filtro opcional para notificaciones leídas/no leídas.
     * @return Un Flux de todas las notificaciones.
     */
    public Flux<Notificacion> obtenerTodasLasNotificaciones(Boolean leida) {
        REGISTRO.debug("Obteniendo todas las notificaciones con estado de lectura: {}", leida);
        if (leida != null) {
            return repositorioNotificaciones.findByLeida(leida); //
        }
        return repositorioNotificaciones.findAll(); //
    }
}

