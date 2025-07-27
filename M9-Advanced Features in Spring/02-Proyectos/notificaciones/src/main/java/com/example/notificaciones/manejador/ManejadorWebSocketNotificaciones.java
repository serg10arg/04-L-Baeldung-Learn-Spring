package com.example.notificaciones.manejador;


import com.example.notificaciones.modelo.Notificacion;
import com.fasterxml.jackson.core.JsonProcessingException; // No en fuentes directas, pero necesario para JSON. Se declara como información fuera de las fuentes.
import com.fasterxml.jackson.databind.ObjectMapper; // No en fuentes directas, pero necesario para JSON. Se declara como información fuera de las fuentes.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication; //
import org.springframework.security.core.context.ReactiveSecurityContextHolder; //
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler; //
import org.springframework.web.reactive.socket.WebSocketSession; //
import reactor.core.publisher.Flux; //
import reactor.core.publisher.Mono; //

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; // Para gestionar las sesiones concurrentemente [información_adicional_del_proyecto]

/**
 * Manejador de WebSocket para el envío de notificaciones en tiempo real.
 * Implementa WebSocketHandler para gestionar el ciclo de vida de las conexiones WebSocket.
 * Mantiene un registro de las sesiones de usuario para enviar notificaciones dirigidas.
 */
@Component // Marca la clase como un componente gestionado por Spring [concepto_Spring]
public class ManejadorWebSocketNotificaciones implements WebSocketHandler {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ManejadorWebSocketNotificaciones.class);

    // Mapa para mantener las sesiones WebSocket activas, mapeadas por identificador de usuario.
    // ConcurrentHashMap es thread-safe y adecuada para entornos concurrentes. [información_adicional_del_proyecto]
    private final Map<String, WebSocketSession> sesionesActivas = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper; // Para convertir objetos Notificacion a JSON [información_adicional_del_proyecto]

    public ManejadorWebSocketNotificaciones(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper; // Inicializa ObjectMapper [información_adicional_del_proyecto]
    }

    /**
     * Método principal para manejar la sesión WebSocket.
     * Se invoca cuando se establece una nueva conexión WebSocket.
     *
     * @param sesion La sesión WebSocket activa.
     * @return Un Mono<Void> que representa la finalización del manejo de la sesión.
     */
    @Override //
    public Mono<Void> handle(WebSocketSession sesion) {
        // Extrae el identificador de usuario de la ruta de la URI WebSocket.
        // Ej: /ws/notificaciones/usuario123 -> "usuario123"
        String path = sesion.getHandshakeInfo().getUri().getPath();
        String identificadorUsuario = path.substring(path.lastIndexOf('/') + 1);

        // Verifica si el usuario autenticado coincide con el identificador de usuario en la ruta.
        // Esto es una medida de seguridad adicional para asegurar que un usuario solo se conecte a su propio canal.
        return ReactiveSecurityContextHolder.getContext() // Obtiene el contexto de seguridad reactivo
                .map(contexto -> contexto.getAuthentication())
                .flatMap(auth -> {
                    // Si no hay autenticación, o si el usuario autenticado no es el mismo que en la ruta,
                    // o si no es un ADMIN intentando monitorear, la conexión no es válida.
                    // Para simplificar, aquí solo verificamos que el usuario autenticado coincida con el path.
                    boolean esAutenticadoYCoincide = auth != null && auth.isAuthenticated() && auth.getName().equals(identificadorUsuario);
                    boolean esAdministrador = auth != null && auth.isAuthenticated() && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    if (esAutenticadoYCoincide || (esAdministrador && identificadorUsuario.equals("admin_monitor"))) { // Ejemplo de un canal para admin
                        REGISTRO.info("Conexión WebSocket establecida para el usuario: {}", identificadorUsuario);
                        sesionesActivas.put(identificadorUsuario, sesion); // Registra la sesión activa [información_adicional_del_proyecto]

                        // Escucha los mensajes entrantes (si el cliente envía algo) y los ignora para este proyecto.
                        // Para este proyecto, el servidor solo envía mensajes (notificaciones push).
                        Mono<Void> entrada = sesion.receive()
                                .doOnNext(mensaje -> REGISTRO.warn("Mensaje entrante inesperado de WebSocket de {}: {}", identificadorUsuario, mensaje.getPayloadAsText()))
                                .then();

                        // Manejar la desconexión de la sesión.
                        Mono<Void> salida = sesion.closeStatus()
                                .doOnNext(estado -> {
                                    REGISTRO.info("Conexión WebSocket cerrada para el usuario: {} con estado: {}", identificadorUsuario, estado);
                                    sesionesActivas.remove(identificadorUsuario); // Elimina la sesión al desconectarse [información_adicional_del_proyecto]
                                })
                                .then();

                        return Mono.zip(entrada, salida).then(); // Combina los flujos de entrada y salida
                    } else {
                        REGISTRO.warn("Intento de conexión WebSocket no autorizado para el usuario {} (autenticado: {})", identificadorUsuario, auth != null ? auth.getName() : "N/A");
                        return sesion.close().then(); // Cierra la conexión si no está autorizado
                    }
                })
                .doOnError(error -> REGISTRO.error("Error en el manejo de la sesión WebSocket: {}", error.getMessage()));
    }

    /**
     * Envía una notificación específica a un usuario a través de su conexión WebSocket activa.
     *
     * @param identificadorUsuario El ID del usuario destino.
     * @param notificacion La notificación a enviar.
     * @return Un Mono<Void> que indica la finalización del envío.
     */
    public Mono<Void> enviarNotificacionAUsuario(String identificadorUsuario, Notificacion notificacion) {
        WebSocketSession sesion = sesionesActivas.get(identificadorUsuario);
        if (sesion != null && sesion.isOpen()) {
            try {
                String notificacionJson = objectMapper.writeValueAsString(notificacion); // Convierte la notificación a JSON [información_externa]
                REGISTRO.debug("Intentando enviar notificación JSON: {} al usuario {}", notificacionJson, identificadorUsuario);
                return sesion.send(Mono.just(sesion.textMessage(notificacionJson))) // Envía el mensaje de texto
                        .doOnError(error -> REGISTRO.error("Error al enviar mensaje por WebSocket al usuario {}: {}", identificadorUsuario, error.getMessage()))
                        .doOnSuccess(v -> REGISTRO.info("Notificación enviada con éxito por WebSocket al usuario: {}", identificadorUsuario));
            } catch (JsonProcessingException e) {
                REGISTRO.error("Error al serializar la notificación a JSON: {}", e.getMessage());
                return Mono.error(new IOException("Error al serializar la notificación", e));
            }
        } else {
            REGISTRO.warn("No se encontró sesión WebSocket activa para el usuario: {}", identificadorUsuario);
            return Mono.empty(); // No hay sesión activa para este usuario, no se hace nada
        }
    }

    /**
     * Envía una notificación a todos los usuarios conectados (útil para notificaciones globales, si aplica).
     * En este proyecto, aunque la funcionalidad principal son las notificaciones dirigidas, esta utilidad puede ser extendida.
     *
     * @param notificacion La notificación a enviar.
     * @return Un Flux<Void> que indica el estado de la operación para cada sesión.
     */
    public Flux<Void> enviarNotificacionATodos(Notificacion notificacion) {
        return Flux.fromIterable(sesionesActivas.values()) // Itera sobre todas las sesiones activas
                .flatMap(sesion -> {
                    if (sesion.isOpen()) {
                        try {
                            String notificacionJson = objectMapper.writeValueAsString(notificacion);
                            return sesion.send(Mono.just(sesion.textMessage(notificacionJson)))
                                    .doOnError(error -> REGISTRO.error("Error al enviar mensaje global por WebSocket a sesión {}: {}", sesion.getId(), error.getMessage()));
                        } catch (JsonProcessingException e) {
                            REGISTRO.error("Error al serializar la notificación global a JSON: {}", e.getMessage());
                            return Mono.error(new IOException("Error al serializar la notificación global", e));
                        }
                    }
                    return Mono.empty(); // Ignora sesiones cerradas
                });
    }
}

